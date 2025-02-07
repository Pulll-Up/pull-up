package com.pullup.game.dto.request;

import java.util.List;

public record CardSubmitRequest(
        String roomId,
        Long playerId,
        int problemNumber,
        List<String> contents
) {
}
