package com.pullup.game.controller;

import com.pullup.game.domain.GameRoomStatus;
import com.pullup.game.dto.request.SubmitCardRequest;
import com.pullup.game.dto.response.GameRoomInfoWithProblemsResponse;
import com.pullup.game.dto.response.GameRoomResultResponse;
import com.pullup.game.dto.response.GetGameRoomStatusResponse;
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

    @MessageMapping("/game/{roomId}/status")
    @SendTo("/topic/game/{roomId}/status")
    public GetGameRoomStatusResponse getRoomStatus(@DestinationVariable String roomId) {
        GameRoomStatus gameRoomStatus = gameService.getGameRoomStatus(roomId);

        return GetGameRoomStatusResponse.of(gameRoomStatus.name());
    }

    @MessageMapping("/card/check")
    public void submitCard(@Payload SubmitCardRequest submitCardRequest,
                           SimpMessageHeaderAccessor headerAccessor) {

        String sessionId = headerAccessor.getSessionId();  // 세션 ID 가져오기

        GameRoomInfoWithProblemsResponse gameRoomInfoWithProblemsResponse =
                gameService.checkTypeAndProcessCardSubmissionOrTimeout(submitCardRequest, sessionId);

        String destination = "/topic/game/" + submitCardRequest.roomId();
        messagingTemplate.convertAndSend(destination, gameRoomInfoWithProblemsResponse);
    }

    @MessageMapping("/game/{roomId}/result")
    public void getGameRoomResult(@DestinationVariable String roomId) {
        // 게임 결과 가져오기
        GameRoomResultResponse gameRoomResultResponse = gameService.getGameRoomResult(roomId);

        // 게임 방 삭제
        gameService.deleteGameRoomWithPlayerId(roomId);

        // 구독 중인 클라이언트들에게 결과 전송
        String destination = "/topic/game/" + roomId + "/result";
        messagingTemplate.convertAndSend(destination, gameRoomResultResponse);
    }

}
