package com.pullup.game.dto;

import com.pullup.game.domain.Player;
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

    public static PlayerInfo from(Player player) {
        return PlayerInfo.builder()
                .memberId(player.getId())
                .name(player.getName())
                .score(player.getScore())
                .build();
    }
}