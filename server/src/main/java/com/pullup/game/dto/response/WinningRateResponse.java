package com.pullup.game.dto.response;

import lombok.Builder;

@Builder
public record WinningRateResponse(
        Integer winCount,
        Integer loseCount,
        Integer drawCount

) {
    public static WinningRateResponse of(Integer winCount, Integer loseCount, Integer drawCount) {
        return WinningRateResponse.builder()
                .winCount(winCount)
                .loseCount(loseCount)
                .drawCount(drawCount)
                .build();
    }
}
