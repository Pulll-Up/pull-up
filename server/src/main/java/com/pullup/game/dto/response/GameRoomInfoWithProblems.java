package com.pullup.game.dto.response;

import com.pullup.game.dto.PlayerInfo;
import com.pullup.game.dto.ProblemCard;
import java.util.List;

public record GameRoomInfoWithProblems(
        String roomId,
        PlayerInfo player1P,
        PlayerInfo player2P,
        List<ProblemCard> problems
) {
}
