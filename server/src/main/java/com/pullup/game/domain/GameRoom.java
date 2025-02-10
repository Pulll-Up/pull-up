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
    //    private GameRoomResultStatus gameRoomResultStatus;
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

    public Player getPlayerByPlayerNumber(Long playerNumber) {
        if (playerNumber == 1L) {
            return player1;
        } else if (playerNumber == 2L) {
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


}

