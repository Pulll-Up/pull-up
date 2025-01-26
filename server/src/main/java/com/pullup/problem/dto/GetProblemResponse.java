package com.pullup.problem.dto;

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
        Subject subject
) {

    public static GetProblemResponse of(Problem problem, List<String> options) {
        return GetProblemResponse.builder()
                .question(problem.getQuestion())
                .options(options)
                .answer(problem.getAnswer())
                .explanation(problem.getExplanation())
                .correctRate(problem.getCorrectRate())
                .subject(problem.getSubject())
                .build();
    }
}
