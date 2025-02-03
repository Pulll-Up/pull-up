package com.pullup.game.controller;

import com.pullup.game.domain.GameRoomStatus;
import com.pullup.game.dto.request.CardSubmitRequest;
import com.pullup.game.dto.response.GameRoomInfoWithProblems;
import com.pullup.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameWebSocketController {
    private final GameService gameService;

    @MessageMapping("/game/{roomId}/status")
    @SendTo("/topic/game/{roomId}/status")
    public GameRoomStatus getRoomStatus(String roomId) {
        System.out.println("getRoomStatus 호출됨! roomId: " + roomId);
        GameRoomStatus gameRoomStatus = gameService.getGameRoomStatus(roomId);

        return gameRoomStatus;
    }

    @MessageMapping("/card/submit")
    @SendTo("/topic/game/{cardSubmitRequest.roomId}")
    public GameRoomInfoWithProblems submitCard(CardSubmitRequest cardSubmitRequest) {
        System.out.println("submitCard 호출됨! roomId: " + cardSubmitRequest.roomId());

        GameRoomInfoWithProblems gameRoomInfoWithProblems = gameService.processCardSubmission(cardSubmitRequest);

        return gameRoomInfoWithProblems;
    }


}
