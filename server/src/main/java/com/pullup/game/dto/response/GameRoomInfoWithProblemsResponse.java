package com.pullup.game.dto.response;

import com.pullup.game.dto.PlayerInfo;
import com.pullup.game.dto.ProblemCard;
import java.util.List;
import lombok.Builder;

@Builder
public record GameRoomInfoWithProblems(
        String roomId,
        PlayerInfo player1P,
        PlayerInfo player2P,
        List<ProblemCard> problems
) {
    public static GameRoomInfoWithProblems of(String roomId, PlayerInfo player1P, PlayerInfo player2P,
                                              List<ProblemCard> problems) {
        return GameRoomInfoWithProblems.builder()
                .roomId(roomId)
                .player1P(player1P)
                .player2P(player2P)
                .problems(problems)
                .build();
    }
}