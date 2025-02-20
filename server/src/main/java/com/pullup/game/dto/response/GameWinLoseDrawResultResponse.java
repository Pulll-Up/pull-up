package com.pullup.game.dto.response;

import lombok.Builder;

@Builder
public record GameWinLoseDrawResultResponse(
        Integer winCount,
        Integer loseCount,
        Integer drawCount

) {
    public static GameWinLoseDrawResultResponse of(Integer winCount, Integer loseCount, Integer drawCount) {
        return GameWinLoseDrawResultResponse.builder()
                .winCount(winCount)
                .loseCount(loseCount)
                .drawCount(drawCount)
                .build();
    }
}
