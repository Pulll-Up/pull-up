package com.pullup.game.dto.response;

public record GetGameWinningRateResponse(
        Integer winningRate
) {

    public static GetGameWinningRateResponse of(Integer winningRate) {
        return new GetGameWinningRateResponse(winningRate);
    }
}
