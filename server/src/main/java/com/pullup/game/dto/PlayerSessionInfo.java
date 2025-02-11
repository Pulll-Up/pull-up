package com.pullup.game.dto;

import com.pullup.game.dto.response.PlayerType;

public record PlayerSessionInfo(
        String roomId,
        PlayerType playerType
) {
    public static PlayerSessionInfo of(String roomId, PlayerType playerType) {
        return new PlayerSessionInfo(roomId, playerType);
    }
}
