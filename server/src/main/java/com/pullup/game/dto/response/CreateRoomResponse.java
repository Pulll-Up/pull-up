package com.pullup.game.dto.response;

public record CreateRoomResponse(
        String roomId
) {
    public static CreateRoomResponse of(String roomId) {
        return new CreateRoomResponse(roomId);
    }
}
