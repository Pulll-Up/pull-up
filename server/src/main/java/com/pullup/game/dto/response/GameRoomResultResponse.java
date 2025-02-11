package com.pullup.game.dto.response;

import com.pullup.game.dto.GameRoomResultStatus;
import lombok.Builder;

@Builder
public record GameRoomResultResponse(
        GameRoomResultStatus gameRoomResultStatus,
        boolean isForfeitGame,
        String name,
        int score,
        String opponentName,
        int opponentScore
) {
    public static GameRoomResultResponse of(
            GameRoomResultStatus gameRoomResultStatus,
            boolean isForfeitGame,
            String name,
            int score,
            String opponentName,
            int opponentScore
    ) {
        return GameRoomResultResponse.builder()
                .gameRoomResultStatus(gameRoomResultStatus)
                .isForfeitGame(isForfeitGame)
                .name(name)
                .score(score)
                .opponentName(opponentName)
                .opponentScore(opponentScore)
                .build();
    }
}
