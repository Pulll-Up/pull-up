package com.pullup.game.dto.response;

public record JoinRoomResponse(
        Boolean isReady
) {

    public static JoinRoomResponse of(Boolean isReady) {
        return new JoinRoomResponse(isReady);
    }
}
