package com.pullup.game.dto.request;

import java.util.List;

public record SubmitCardRequest(
        String roomId,
        Long playerId,
        int problemNumber,
        List<String> contents,
        CheckType checkType
) {
}
