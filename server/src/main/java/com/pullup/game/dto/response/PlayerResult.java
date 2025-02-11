package com.pullup.game.dto.response;

import com.pullup.game.dto.GameRoomResultStatus;
import lombok.Builder;

@Builder
public record PlayerResult(
        String name,
        int score,
        GameRoomResultStatus status
) {
    public static PlayerResult of(String name, int score, GameRoomResultStatus status) {
        return PlayerResult.builder()
                .name(name)
                .score(score)
                .status(status)
                .build();
    }
}
