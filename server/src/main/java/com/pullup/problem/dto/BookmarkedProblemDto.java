package com.pullup.problem.dto;

import com.pullup.problem.domain.Bookmark;
import com.pullup.problem.domain.Subject;
import java.time.LocalDateTime;
import lombok.Builder;

public record BookmarkedProblemDto(
        Long problemId,
        String question,
        Subject subject,
        LocalDateTime date
) {
    @Builder
    public BookmarkedProblemDto {
    }

    public static BookmarkedProblemDto of(Bookmark bookmark) {
        return BookmarkedProblemDto.builder()
                .problemId(bookmark.getProblem().getId())
                .question(bookmark.getProblem().getQuestion())
                .subject(bookmark.getProblem().getSubject())
                .date(bookmark.getModifiedAt())
                .build();
    }
}
