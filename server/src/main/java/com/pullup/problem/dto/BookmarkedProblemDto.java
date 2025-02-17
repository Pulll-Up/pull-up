package com.pullup.problem.dto;

import com.pullup.problem.domain.Bookmark;
import com.pullup.problem.domain.Subject;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record BookmarkedProblemDto(
        String problemId,
        String question,
        Subject subject,
        LocalDateTime date
) {
    public static BookmarkedProblemDto of(String problemId, Bookmark bookmark) {
        return BookmarkedProblemDto.builder()
                .problemId(problemId)
                .question(bookmark.getProblem().getQuestion())
                .subject(bookmark.getProblem().getSubject())
                .date(bookmark.getModifiedAt())
                .build();
    }
}
