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

    public static GameRoom craeteGameRoomWithHost(Long id, String name) {
        return GameRoom.builder()
                .roomId(generateUniqueRoomCode())
                .player1(Player.createNewPlayer(id, name))
                .status(GameStatus.WAITING)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void addGuest(Long id, String name) {
        this.player2 = Player.createNewPlayer(id, name);
        updateStatusToPlaying();
    }

    private static String generateUniqueRoomCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    private void updateStatusToPlaying() {
        this.status = GameStatus.PLAYING;
    }

}

