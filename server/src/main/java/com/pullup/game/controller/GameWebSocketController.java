package com.pullup.game.controller;

import com.pullup.game.domain.GameRoomStatus;
import com.pullup.game.dto.request.SubmitCardRequest;
import com.pullup.game.dto.response.GameRoomInfoWithProblemsResponse;
import com.pullup.game.dto.response.GetGameRoomStatusResponse;
import com.pullup.game.repository.WebSocketSessionRepository;
import com.pullup.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameWebSocketController {

    private final GameService gameService;
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketSessionRepository webSocketSessionRepository;

    @MessageMapping("/game/{roomId}/status")
    @SendTo("/topic/game/{roomId}/status")
    public GetGameRoomStatusResponse getRoomStatus(@DestinationVariable String roomId) {
        GameRoomStatus gameRoomStatus = gameService.getGameRoomStatus(roomId);

        return GetGameRoomStatusResponse.of(gameRoomStatus.name());
    }

    @MessageMapping("/card/check")
    public void submitCard(@Payload SubmitCardRequest submitCardRequest,
                           SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();

        // 세션에 roomId 저장
        headerAccessor.getSessionAttributes().put("roomId", submitCardRequest.roomId());
        webSocketSessionRepository.save(sessionId, submitCardRequest.playerId());

        GameRoomInfoWithProblemsResponse gameRoomInfoWithProblemsResponse =
                gameService.checkTypeAndProcessCardSubmissionOrTimeout(submitCardRequest);

        String destination = "/topic/game/" + submitCardRequest.roomId();
        messagingTemplate.convertAndSend(destination, gameRoomInfoWithProblemsResponse);
    }

}
