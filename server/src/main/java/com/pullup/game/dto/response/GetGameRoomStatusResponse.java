package com.pullup.game.dto.response;

public record GetGameRoomStatusResponse(
        String status
) {
    public static GetGameRoomStatusResponse of(String status) {
        return new GetGameRoomStatusResponse(status);
    }
}
