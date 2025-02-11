package com.pullup.game.dto.response;

public record GetPlayerNumberResponse(
        PlayerType playerType;
) {
    public static GetPlayerNumberResponse of(Long playerType) {
        return new GetPlayerNumberResponse(playerType);
    }
}
