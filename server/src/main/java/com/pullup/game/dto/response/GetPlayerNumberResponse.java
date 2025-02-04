package com.pullup.game.dto.response;

public record GetPlayerNumberResponse(
        Long playerNumber
) {
    public static GetPlayerNumberResponse of(Long playerNumber) {
        return new GetPlayerNumberResponse(playerNumber);
    }
}
