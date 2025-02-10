package com.pullup.game.dto;

import com.pullup.game.domain.GameRoomStatus;

public record GameRoomStatusRequest(
        GameRoomStatus gameRoomStatus
) {
}
