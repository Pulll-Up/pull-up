package com.pullup.game.domain;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.game.dto.GameRoomResultStatus;
import com.pullup.game.dto.response.PlayerType;
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
    private GameRoomResultStatus gameRoomResultStatus;
    private Boolean isForfeitGame;
    private Player winner;
    private GameRoomStatus gameRoomStatus;
    private GameRoomType gameRoomType;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public static GameRoom createGameRoomByInvitationWithHost(Long id, String name) {
        return GameRoom.builder()
                .roomId(generateUniqueRoomCode())
                .player1(Player.createNewPlayer(id, name))
                .isForfeitGame(false)
                .winner(null)
                .gameRoomStatus(GameRoomStatus.WAITING)
                .gameRoomType(GameRoomType.INVITATION)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }

    public static GameRoom createGameRoomByRandomMatchingWithHost(Long id, String name) {
        return GameRoom.builder()
                .roomId(generateUniqueRoomCode())
                .player1(Player.createNewPlayer(id, name))
                .isForfeitGame(false)
                .winner(null)
                .gameRoomStatus(GameRoomStatus.WAITING)
                .gameRoomType(GameRoomType.RANDOM_MATCHING)
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
        this.gameRoomStatus = GameRoomStatus.PLAYING;
    }

    public void updateStatusToFinished() {
        this.gameRoomStatus = GameRoomStatus.FINISHED;
    }

    public Player getPlayerByPlayerType(PlayerType playerType) {
        if (playerType == PlayerType.player1P) {
            return player1;
        } else if (playerType == PlayerType.player2P) {
            return player2;
        } else {
            throw new NotFoundException(ErrorMessage.ERR_PLAYER_NOT_FOUND);
        }
    }

    public void updateWinner(Player player) {
        this.winner = player;
    }

    public void updateToForfeitGame() {
        this.isForfeitGame = true;
    }

    public Player getOpponentPlayerByPlayerType(PlayerType playerType) {
        if (playerType == PlayerType.player1P) {
            return player2;
        } else if (playerType == PlayerType.player2P) {
            return player1;
        } else {
            throw new NotFoundException(ErrorMessage.ERR_PLAYER_NOT_FOUND);
        }
    }

}

