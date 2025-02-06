package com.pullup.game.controller;

import com.pullup.game.domain.GameRoomStatus;
import com.pullup.game.dto.request.CardSubmitRequest;
import com.pullup.game.dto.response.GameRoomInfoWithProblemsResponse;
import com.pullup.game.dto.response.GetGameRoomStatusResponse;
import com.pullup.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameWebSocketController {

    private final GameService gameService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/game/{roomId}/status")
    @SendTo("/topic/game/{roomId}/status")
    public GetGameRoomStatusResponse getRoomStatus(@DestinationVariable String roomId) {
        GameRoomStatus gameRoomStatus = gameService.getGameRoomStatus(roomId);

        return GetGameRoomStatusResponse.of(gameRoomStatus.name());
    }

//    @SubscribeMapping("/topic/game/{roomId}")
//    public GameRoomInfoWithProblemsResponse sendInitialGameInfo(@DestinationVariable String roomId) {
//        GameRoomInfoWithProblemsResponse gameRoomInfoWithProblemsResponse = gameService.getInitialGameRoomInfo(roomId);
//        System.out.println("들어오는지 확인");
//        return gameRoomInfoWithProblemsResponse;
//    }


    @MessageMapping("/card/submit")
    public void submitCard(@Payload CardSubmitRequest cardSubmitRequest) {
        GameRoomInfoWithProblemsResponse gameRoomInfoWithProblemsResponse = gameService.processCardSubmission(
                cardSubmitRequest);

        // 동적으로 해당 방의 구독 경로에 메시지 전송
        String destination = "/topic/game/" + cardSubmitRequest.roomId();
        messagingTemplate.convertAndSend(destination, gameRoomInfoWithProblemsResponse);
    }

}
