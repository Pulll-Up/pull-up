package com.pullup.game.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetGameRoomStatusResponse(
        @JsonProperty("status")
        String status
) {
    public static GetGameRoomStatusResponse of(String status) {
        return new GetGameRoomStatusResponse(status);
    }
}
