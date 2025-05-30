package com.pullup.game.dto.response;

import com.pullup.game.domain.GameRoomStatus;
import com.pullup.game.dto.PlayerInfo;
import com.pullup.game.dto.ProblemCardWithoutCardId;
import com.pullup.game.dto.request.CheckType;
import java.util.List;
import lombok.Builder;

@Builder
public record GameRoomInfoWithProblemsResponse(
        String roomId,
        GameRoomStatus gameRoomStatus,
        PlayerInfo player1P,
        PlayerInfo player2P,
        List<ProblemCardWithoutCardId> problemCardWithoutCardIds,
        CheckType checkType
) {
    public static GameRoomInfoWithProblemsResponse of(
            String roomId,
            GameRoomStatus gameRoomStatus,
            PlayerInfo player1P,
            PlayerInfo player2P,
            List<ProblemCardWithoutCardId> problemCardWithoutCardIds,
            CheckType checkType
    ) {
        return GameRoomInfoWithProblemsResponse.builder()
                .roomId(roomId)
                .gameRoomStatus(gameRoomStatus)
                .player1P(player1P)
                .player2P(player2P)
                .problemCardWithoutCardIds(problemCardWithoutCardIds)
                .checkType(checkType)
                .build();
    }
    
}