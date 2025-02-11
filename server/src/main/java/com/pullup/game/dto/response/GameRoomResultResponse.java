package com.pullup.game.dto.response;

import lombok.Builder;

@Builder
public record GameRoomResultResponse(
        boolean isDraw,
        boolean isForfeit,
        PlayerResult player1P,
        PlayerResult player2P
) {
    public static GameRoomResultResponse of(
            boolean isDraw,
            boolean isForfeit,
            PlayerResult player1P,
            PlayerResult player2P
    ) {
        return GameRoomResultResponse.builder()
                .isDraw(isDraw)
                .isForfeit(isForfeit)
                .player1P(player1P)
                .player2P(player2P)
                .build();
    }
}
