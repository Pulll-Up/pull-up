package com.pullup.problem.dto.response;

import com.pullup.problem.dto.BookmarkedProblemDto;
import java.util.List;

public record SearchBookmarkedProblemsResponse(
        List<BookmarkedProblemDto> bookmarkedProblemDtos
) {
    public static SearchBookmarkedProblemsResponse of(List<BookmarkedProblemDto> bookmarkedProblemDtos) {
        return new SearchBookmarkedProblemsResponse(bookmarkedProblemDtos);
    }
}
