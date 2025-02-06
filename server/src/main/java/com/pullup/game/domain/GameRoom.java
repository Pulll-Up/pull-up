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
    private GameRoomStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public static GameRoom craeteGameRoomWithHost(Long id, String name) {
        return GameRoom.builder()
                .roomId(generateUniqueRoomCode())
                .player1(Player.createNewPlayer(id, name))
                .status(GameRoomStatus.WAITING)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
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
        this.status = GameRoomStatus.PLAYING;
    }

    public void updateStatusToFinished() {
        this.status = GameRoomStatus.FINISHED;
    }

    public Player getPlayerByPlayerId(Long playerId) {
        if (playerId == 1L) {
            return player1;
        } else if (playerId == 2L) {
            return player2;
        } else {
            throw new NotFoundException(ErrorMessage.ERR_PLAYER_NOT_FOUND);
        }
    }


}

