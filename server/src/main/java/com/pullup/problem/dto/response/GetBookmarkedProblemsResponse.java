package com.pullup.problem.dto.response;

import com.pullup.problem.dto.BookmarkedProblemDto;
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