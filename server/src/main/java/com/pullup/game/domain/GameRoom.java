package com.pullup.game.domain;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
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

    // ✅ playerId로 플레이어 찾기
    public Player getPlayerById(Long playerId) {
        if (player1.getId().equals(playerId)) {
            return player1;
        } else if (player2.getId().equals(playerId)) {
            return player2;
        } else {
            throw new NotFoundException(ErrorMessage.ERR_PLAYER_NOT_FOUND);
        }
    }

}

