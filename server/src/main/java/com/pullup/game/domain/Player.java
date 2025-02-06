package com.pullup.game.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Player {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    private Integer score;

    public static Player createNewPlayer(Long id, String name) {
        return Player.builder()
                .id(id)
                .name(name)
                .score(0)
                .build();
    }

    public void increaseScore() {
        this.score++;
    }
}