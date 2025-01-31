package com.pullup.game.domain;

import lombok.Builder;

@Builder
public class Player {
    private String id;
    private String name;
    private Integer score;

    public static Player of(String id, String name) {
        return Player.builder()
                .id(id)
                .name(name)
                .score(0)
                .build();
    }

    public void incrementScore() {
        this.score++;
    }
}