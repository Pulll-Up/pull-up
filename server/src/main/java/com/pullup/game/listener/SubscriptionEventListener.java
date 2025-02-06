package com.pullup.game.listener;

import com.pullup.game.dto.response.GameRoomInfoWithProblemsResponse;
import com.pullup.game.service.GameService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
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

        // 특정 토픽(/topic/game/{roomId})에 대한 구독인지 확인
        if (destination != null && destination.startsWith("/topic/game/")) {
            String roomId = destination.substring("/topic/game/".length());

            // 초기 게임 데이터 가져오기
            GameRoomInfoWithProblemsResponse initialData = gameService.getInitialGameRoomInfo(roomId);

            // 해당 토픽으로 초기 데이터 전송
            messagingTemplate.convertAndSend(destination, initialData);
        }
    }
}
