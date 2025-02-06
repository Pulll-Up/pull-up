package com.pullup.game.dto;

import lombok.Builder;

@Builder
public record PlayerInfo(
        Long memberId,
        String name,
        Integer score
) {
    public static PlayerInfo of(Long memberId, String name, Integer score) {
        return PlayerInfo.builder()
                .memberId(memberId)
                .name(name)
                .score(score)
                .build();
    }
}