package com.pullup.game.controller;

import com.pullup.common.util.SecurityUtil;
import com.pullup.game.dto.request.CreateRoomWithSubjectsRequest;
import com.pullup.game.dto.request.JoinRoomRequest;
import com.pullup.game.dto.response.CreateRoomResponse;
import com.pullup.game.dto.response.GetGameWinningRateResponse;
import com.pullup.game.dto.response.GetPlayerNumberResponse;
import com.pullup.game.dto.response.JoinRoomResponse;
import com.pullup.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/room")
    public ResponseEntity<CreateRoomResponse> createRoom(@RequestBody CreateRoomWithSubjectsRequest request) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        CreateRoomResponse createRoomResponse = gameService.createRoom(memberId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createRoomResponse);
    }

    @PostMapping("/room/join")
    public ResponseEntity<JoinRoomResponse> joinRoom(@RequestBody JoinRoomRequest JoinRoomRequest) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();

        // 게임방에 사용자 추가
        JoinRoomResponse joinRoomResponse = gameService.join(JoinRoomRequest.roomId(), memberId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(joinRoomResponse);
    }

    @GetMapping("/room/{roomId}/player")
    public ResponseEntity<GetPlayerNumberResponse> getPlayerType(@PathVariable String roomId) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();

        GetPlayerNumberResponse getPlayerNumberResponse = gameService.getPlayerNumber(roomId, memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getPlayerNumberResponse);
    }

    @GetMapping("/me/winning-rate")
    public ResponseEntity<GetGameWinningRateResponse> getGameWinningRateResponse() {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        GetGameWinningRateResponse getGameWinningRateResponse = gameService.getGameWinningRateResponse(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getGameWinningRateResponse);
    }
}