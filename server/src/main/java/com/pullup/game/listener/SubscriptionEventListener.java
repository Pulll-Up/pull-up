package com.pullup.game.listener;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.game.domain.GameRoom;
import com.pullup.game.domain.Player;
import com.pullup.game.dto.PlayerSessionInfo;
import com.pullup.game.dto.response.GameRoomInfoWithProblemsResponse;
import com.pullup.game.dto.response.PlayerType;
import com.pullup.game.repository.GameRoomRepository;
import com.pullup.game.repository.WebSocketSessionManager;
import com.pullup.game.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final GameService gameService;
    private final GameRoomRepository gameRoomRepository;
    private final WebSocketSessionManager sessionManager;


    @EventListener
    public void handleSubscription(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();

        if (destination != null && destination.startsWith("/topic/game/")) {
            String roomId = destination.substring("/topic/game/".length());

            GameRoomInfoWithProblemsResponse gameRoomInfoWithProblemsResponse = gameService.getInitialGameRoomInfo(
                    roomId);

            messagingTemplate.convertAndSend(destination, gameRoomInfoWithProblemsResponse);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        PlayerSessionInfo sessionInfo = sessionManager.removeSession(sessionId); // 세션 삭제 후 roomId, playerType 반환

        if (sessionInfo != null) {
            String roomId = sessionInfo.roomId();
            PlayerType playerType = sessionInfo.playerType();

            log.info("❌ {}님이 {} 방에서 이탈 (세션 ID: {})", playerType, roomId, sessionId);

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

//                // 남은 유저에게 게임 종료 메시지 전송
//                messagingTemplate.convertAndSend(
//                        "/topic/game/" + roomId,
//                        GameStatusResponse.of(roomId, opponent.getId(), "승리 (상대방 이탈)")
//                );
            }
        }
    }


}
