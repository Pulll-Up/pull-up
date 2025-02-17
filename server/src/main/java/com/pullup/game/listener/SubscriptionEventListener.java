package com.pullup.game.listener;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.IllegalArgumentException;
import com.pullup.common.exception.NotFoundException;
import com.pullup.game.domain.GameRoom;
import com.pullup.game.domain.GameRoomStatus;
import com.pullup.game.domain.Player;
import com.pullup.game.dto.PlayerInfo;
import com.pullup.game.dto.PlayerSessionInfo;
import com.pullup.game.dto.ProblemCard;
import com.pullup.game.dto.response.GameRoomInfoWithProblemsResponse;
import com.pullup.game.dto.response.PlayerType;
import com.pullup.game.repository.GameRoomRepository;
import com.pullup.game.repository.WebSocketSessionManager;
import com.pullup.game.service.GameService;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final GameService gameService;
    private final GameRoomRepository gameRoomRepository;
    private final WebSocketSessionManager sessionManager;

    private final ConcurrentMap<String, AtomicInteger> roomSubscribers = new ConcurrentHashMap<>();

    @EventListener
    public void handleSubscription(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();

        if (destination != null && destination.startsWith("/topic/game/")
                && !destination.endsWith("/status")
                && !destination.endsWith("/result")
        ) {
            String roomId = destination.substring("/topic/game/".length());

            // 구독자 수 증가
            roomSubscribers.putIfAbsent(roomId, new AtomicInteger(0));
            int subscriberCount = roomSubscribers.get(roomId).incrementAndGet();

            log.info("[{}번 방] 구독 발생 - 현재 구독자 수: {}", roomId, subscriberCount);

            // 구독자가 2명 이상이면 메시지 전송
            if (subscriberCount >= 2) {
                GameRoomInfoWithProblemsResponse gameRoomInfoWithProblemsResponse = gameService.getInitialGameRoomInfo(
                        roomId);
                messagingTemplate.convertAndSend(destination, gameRoomInfoWithProblemsResponse);
            }
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        String sessionId = event.getSessionId();
        PlayerSessionInfo sessionInfo = sessionManager.removeSession(sessionId); // 세션 삭제 후 roomId, playerType 반환

        if (sessionInfo != null) {
            String roomId = sessionInfo.roomId();
            PlayerType playerType = sessionInfo.playerType();

            log.info("{}님이 {} 방에서 이탈 (세션 ID: {})", playerType, roomId, sessionId);

            // 구독자 수 감소 처리
            roomSubscribers.computeIfPresent(roomId, (key, count) -> {
                int newCount = count.decrementAndGet();
                log.info("⚠[{}번 방] 남은 구독자 수: {}", roomId, newCount);
                return newCount > 0 ? count : null; // 구독자가 0명이면 삭제
            });

            // 게임 방 찾기
            GameRoom gameRoom = gameRoomRepository.findByRoomId(sessionInfo.roomId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_GAME_ROOM_NOT_FOUND));

            if (gameRoom != null) {
                // 상대방을 승자로 처리
                Player opponent = gameRoom.getOpponentPlayerByPlayerType(playerType);

                gameRoom.updateStatusToFinished();
                gameRoom.updateWinner(opponent);
                gameRoom.updateToForfeitGame();
                gameRoomRepository.save(gameRoom);

                List<ProblemCard> problemCards = gameService.getProblemsByRoomId(gameRoom.getRoomId());

                GameRoomInfoWithProblemsResponse gameRoomInfoWithProblemsResponse = GameRoomInfoWithProblemsResponse.of(
                        gameRoom.getRoomId(),
                        GameRoomStatus.FINISHED,
                        PlayerInfo.of(
                                gameRoom.getPlayer1().getId(),
                                gameRoom.getPlayer1().getName(),
                                gameRoom.getPlayer1().getScore()),
                        PlayerInfo.of(
                                gameRoom.getPlayer2().getId(),
                                gameRoom.getPlayer2().getName(),
                                gameRoom.getPlayer2().getScore()),
                        gameService.convertToProblemCardWithoutCardIds(problemCards)
                );

                // 남은 유저에게 게임 종료 메시지 전송
                messagingTemplate.convertAndSend(
                        "/topic/game/" + roomId,
                        gameRoomInfoWithProblemsResponse);
            }
        }

    }

    @EventListener
    public void handleUnsubscribe(SessionUnsubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination(); // 구독 해제된 Topic
        String sessionId = headerAccessor.getSessionId(); // 세션 ID

        if (destination == null) {
            return;
        }

        log.info("구독 해제 이벤트 감지 - 세션 ID: {}, 구독 해제된 토픽: {}", sessionId, destination);

        // 특정 게임 방의 상태를 확인하는 토픽인지 검사
        if (destination.startsWith("/topic/game/") && destination.endsWith("/status")) {
            String roomId = extractRoomId(destination);

            // 방의 상태가 `WAITING`이면 삭제
            GameRoomStatus gameRoomStatus = gameService.getGameRoomStatus(roomId);
            if (gameRoomStatus == GameRoomStatus.WAITING) {
                log.info("구독 해제 후 방 상태가 WAITING이므로 방 삭제 - Room ID: {}", roomId);
                gameService.deleteGameRoom(roomId);
            } else {
                log.info("구독 해제되었지만 방 상태가 WAITING이 아님 - Room ID: {}, Status: {}", roomId, gameRoomStatus);
            }
        }
    }

    private String extractRoomId(String topic) {
        String[] parts = topic.split("/");
        if (parts.length < 4) {
            throw new IllegalArgumentException(ErrorMessage.ERR_INTERNAL_SERVER);
        }
        return parts[3];
    }


}
