package com.pullup.game.controller;

import com.pullup.game.domain.GameStatus;
import com.pullup.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameWebSocketController {
    private final GameService gameService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/game/{roomId}/status")
    public void getRoomStatus(String roomId) {
        GameStatus status = gameService.getGameRoomStatus(roomId);
        messagingTemplate.convertAndSend("/topic/game/" + roomId + "/status", status);
    }


}
