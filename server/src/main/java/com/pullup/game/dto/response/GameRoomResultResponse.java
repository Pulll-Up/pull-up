package com.pullup.game.dto.response;

import com.pullup.game.dto.GameRoomResultStatus;

public record GameRoomResultResponse(
        GameRoomResultStatus gameRoomResultStatus,
        String winnerName
) {
    public static GameRoomResultResponse of(GameRoomResultStatus gameRoomResultStatus, String winnerName) {
        return new GameRoomResultResponse(gameRoomResultStatus, winnerName);
    }
}
