package com.pullup.problem.dto.response;

import com.pullup.problem.domain.Bookmark;
import com.pullup.problem.domain.Problem;
import com.pullup.problem.domain.Subject;
import java.util.List;
import lombok.Builder;

@Builder
public record GetProblemResponse(
        String question,
        List<String> options,
        String answer,
        String explanation,
        Integer correctRate,
        Subject subject,
        Boolean bookmarkStatus
) {

    public static GetProblemResponse of(Problem problem, List<String> options, Bookmark bookmark) {
        return GetProblemResponse.builder()
                .question(problem.getQuestion())
                .options(options)
                .answer(problem.getAnswer())
                .explanation(problem.getExplanation())
                .correctRate(problem.getCorrectRate())
                .subject(problem.getSubject())
                .bookmarkStatus(bookmark.getIsBookmarked())
                .build();
    }
}
