package com.pullup.game.controller;

import com.pullup.game.domain.GameRoomStatus;
import com.pullup.game.dto.GameRoomStatusRequest;
import com.pullup.game.dto.request.SubmitCardRequest;
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
    public GetGameRoomStatusResponse handleRoomStatus(@DestinationVariable String roomId,
                                                      @Payload(required = false) GameRoomStatusRequest gameRoomStatusRequest) {
        GameRoomStatus status = gameService.handleGameRoomStatus(roomId, request);
        return GetGameRoomStatusResponse.of(status.name());
    }

    @MessageMapping("/card/submit")
    public void submitCard(@Payload SubmitCardRequest submitCardRequest) {
        GameRoomInfoWithProblemsResponse gameRoomInfoWithProblemsResponse = gameService.processCardSubmission(
                submitCardRequest);

        String destination = "/topic/game/" + submitCardRequest.roomId();
        messagingTemplate.convertAndSend(destination, gameRoomInfoWithProblemsResponse);
    }

}
