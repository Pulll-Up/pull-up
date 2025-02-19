package com.pullup.game.dto.response;

public record WinningRateResponse(
        Integer winningRate
) {
    public static WinningRateResponse of(Integer winningRate) {
        return new WinningRateResponse(winningRate);
    }
}
