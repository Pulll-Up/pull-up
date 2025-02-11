package com.pullup.game.dto.response;

public record GetPlayerTypeResponse(
        PlayerType playerType
) {
    public static GetPlayerTypeResponse of(PlayerType playerType) {
        return new GetPlayerTypeResponse(playerType);
    }
}
