package com.pullup.game.dto.response;

public record JoinRoomResponse(
        Boolean isReady
) {

    public static JoinRoomResponse success() {
        return new JoinRoomResponse(true);
    }
}
