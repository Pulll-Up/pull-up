package com.pullup.game.dto.request;

public record JoinRoomRequest(
        String roomId

) {
    public static JoinRoomRequest of(String roomId) {
        return new JoinRoomRequest(roomId);
    }
}
