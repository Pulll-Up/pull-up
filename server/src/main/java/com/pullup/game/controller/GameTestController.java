package com.pullup.game.controller;

import com.pullup.game.domain.GameRoom;
import com.pullup.game.repository.GameRoomRepository;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/game-test")
public class GameTestController implements GameTestApi {
    private final GameRoomRepository gameRoomRepository;

    @GetMapping("/all")
    public ResponseEntity<Collection<GameRoom>> getAllRooms() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(gameRoomRepository.findAll());
    }
}
