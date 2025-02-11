package com.pullup.problem.dto;

import java.util.List;
import lombok.Builder;

public record GetBookmarkedProblemsResponse(
        List<BookmarkedProblemDto> bookmarkedProblemDtos
) {
    @Builder
    public GetBookmarkedProblemsResponse {
    }

    public static GetBookmarkedProblemsResponse of(List<BookmarkedProblemDto> bookmarkedProblemDtos) {
        return GetBookmarkedProblemsResponse.builder()
                .bookmarkedProblemDtos(bookmarkedProblemDtos)
                .build();
    }
}