package com.pullup.game.dto;

public record PlayerInfo(
        Long memberId,
        String name,
        int score
) {
}