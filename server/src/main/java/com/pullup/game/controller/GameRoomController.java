package com.pullup.game.controller;

import com.pullup.common.util.SecurityUtil;
import com.pullup.game.dto.response.CreateRoomResponse;
import com.pullup.game.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class GameRoomController {

    private final GameRoomService gameRoomService;

    @PostMapping("/room")
    public ResponseEntity<CreateRoomResponse> createRoom() {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        CreateRoomResponse createRoomResponse = gameRoomService.createRoom(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(createRoomResponse);
    }

}