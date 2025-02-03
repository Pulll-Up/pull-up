package com.pullup.game.dto.request;

public record CardSubmitRequest(
        String roomId,
        Long playerId,
        int problemNumber
) {
}
