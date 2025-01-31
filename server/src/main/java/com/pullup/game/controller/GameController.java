package com.pullup.game.controller;

import com.pullup.common.util.SecurityUtil;
import com.pullup.game.dto.response.CreateRoomResponse;
import com.pullup.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/room")
    public ResponseEntity<CreateRoomResponse> createRoom() {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        CreateRoomResponse createRoomResponse = gameService.createRoom(memberId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createRoomResponse);
    }

}