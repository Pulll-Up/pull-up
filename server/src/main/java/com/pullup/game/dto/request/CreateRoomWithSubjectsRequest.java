package com.pullup.game.dto.request;

public record CreateRoomWithSubjectsRequest(
        boolean algorithm,
        boolean computerArchitecture,
        boolean database,
        boolean dataStructure,
        boolean network,
        boolean operatingSystem
) {
}

