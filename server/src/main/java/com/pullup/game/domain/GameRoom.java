package com.pullup.game.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameRoom {
    private String roomId;
    private Player player1;
    private Player player2;
    private GameStatus status;
    private LocalDateTime createdAt;

    public static GameRoom of(String id, String name) {
        return GameRoom.builder()
                .roomId(generateUniqueRoomCode())
                .player1(Player.of(id, name))
                .status(GameStatus.WAITING)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private static String generateUniqueRoomCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}

