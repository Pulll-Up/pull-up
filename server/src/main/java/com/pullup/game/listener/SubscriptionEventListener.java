package com.pullup.game.listener;

import com.pullup.game.dto.response.GameRoomInfoWithProblemsResponse;
import com.pullup.game.service.GameService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class SubscriptionEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final GameService gameService;

    public SubscriptionEventListener(SimpMessageSendingOperations messagingTemplate, GameService gameService) {
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
    }

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
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // 연결이 끊긴 세션의 정보를 가져옵니다
        String sessionId = headerAccessor.getSessionId();

        // 여기서 roomId를 가져오는 방법은 두 가지가 있습니다:
        // 1. 세션에 저장해둔 정보를 활용
        // 2. GameService에서 세션 ID로 룸 정보를 조회

        // 1번 방법을 사용한다면, 연결 시점에 세션에 정보를 저장해두어야 합니다
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        if (roomId != null) {
            // 게임 서비스에서 연결 끊김 처리
            GameRoomInfoWithProblemsResponse response = gameService.handleDisconnection(sessionId, roomId);

            // 같은 방에 있는 다른 사용자들에게 알림
            messagingTemplate.convertAndSend("/topic/game/" + roomId, response);
        }
    }


}
