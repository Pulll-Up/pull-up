package com.pullup.game.dto.request;

import com.pullup.game.dto.response.PlayerType;
import java.util.List;

public record SubmitCardRequest(
        String roomId,
        Long playerNumber,
        PlayerType playerType,
        List<String> contents,
        CheckType checkType
) {
}
