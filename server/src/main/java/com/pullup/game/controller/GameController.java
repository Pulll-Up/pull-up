package com.pullup.game.controller;

import com.pullup.common.util.SecurityUtil;
import com.pullup.game.dto.request.CreateRoomWithSubjectsRequest;
import com.pullup.game.dto.request.JoinRoomRequest;
import com.pullup.game.dto.response.CreateRoomResponse;
import com.pullup.game.dto.response.GetPlayerTypeResponse;
import com.pullup.game.dto.response.GetRandomMatchTypeResponse;
import com.pullup.game.dto.response.JoinRoomResponse;
import com.pullup.game.dto.response.PlayerType;
import com.pullup.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class GameController implements GameApi {

    private final GameService gameService;

    @PostMapping("/room")
    public ResponseEntity<CreateRoomResponse> createRoom(@RequestBody CreateRoomWithSubjectsRequest request) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        CreateRoomResponse createRoomResponse = gameService.createRoom(memberId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createRoomResponse);
    }

    @PostMapping("/room/random")
    public ResponseEntity<CreateRoomResponse> createRoomForRandomMatching() {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        CreateRoomResponse createRoomResponse = gameService.createRoomForRandomMatching(memberId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createRoomResponse);
    }

    @PostMapping("/room/join")
    public ResponseEntity<JoinRoomResponse> joinRoom(@RequestBody JoinRoomRequest JoinRoomRequest) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();

        JoinRoomResponse joinRoomResponse = gameService.join(JoinRoomRequest.roomId(), memberId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(joinRoomResponse);
    }

    @GetMapping("/room/{roomId}/player")
    public ResponseEntity<GetPlayerTypeResponse> getPlayerType(@PathVariable String roomId) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();

        PlayerType playerType = gameService.getPlayerNumberByMemberId(roomId, memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(GetPlayerTypeResponse.of(playerType));
    }

    @GetMapping("/random/type")
    public ResponseEntity<GetRandomMatchTypeResponse> getRandomMatchType() {
        GetRandomMatchTypeResponse getRandomMatchTypeResponse = gameService.getRandomMatchType();

        return ResponseEntity.status(HttpStatus.OK)
                .body(getRandomMatchTypeResponse);
    }

    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Void> deleteGameRoom(@PathVariable("roomId") String roomId) {
        // delete 왔을 떄 방에 2명이 전부 다 있으면, 보낸 사람이 기권해.
        Long memberId = SecurityUtil.getAuthenticatedMemberId();

        gameService.checkForfeitAndDeleteGameRoom(roomId, memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }


}