package com.pullup.problem.controller;

import java.util.List;
import lombok.Builder;

public record GetBookmarkedProblemsResponse(
        List<BookmarkedProblemDto> bookmarkedProblemDtos
) {
    @Builder
    public GetBookmarkedProblemsResponse {
    }

    public static GetBookmarkedProblemsResponse of(List<BookmarkedProblemDto> dtos) {
        return GetBookmarkedProblemsResponse.builder()
                .bookmarkedProblemDtos(dtos)
                .build();
    }
}