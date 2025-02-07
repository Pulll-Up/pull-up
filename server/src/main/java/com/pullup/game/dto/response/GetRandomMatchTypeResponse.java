package com.pullup.game.dto.response;

import com.pullup.game.dto.RandomMatchType;

public record GetRandomMatchTypeResponse(
        RandomMatchType randomMatchType,
        String roomId
) {
    public static GetRandomMatchTypeResponse createForCreateType(RandomMatchType randomMatchType) {
        return new GetRandomMatchTypeResponse(randomMatchType, "");
    }

    public static GetRandomMatchTypeResponse createForJoinType(RandomMatchType randomMatchType, String roomId) {
        return new GetRandomMatchTypeResponse(randomMatchType, roomId);
    }
}
}
