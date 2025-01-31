package com.pullup.exam.dto;

import com.pullup.exam.domain.ExamProblem;
import com.pullup.problem.domain.Problem;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public record ExamResultDetailDto(
        Long problemId,
        String problem,
        List<String> options,
        String chosenAnswer,
        String answer,
        boolean answerStatus,
        boolean bookmarkStatus,
        String explanation,
        int correctRate,
        String subject,
        String problemType
) {
    public static ExamResultDetailDto of(
            ExamProblem examProblem,
            Map<Long, List<String>> problemOptionsMap,
            Map<Long, Boolean> bookmarkStatusMap
    ) {
        Problem problem = examProblem.getProblem();
        return new ExamResultDetailDto(
                problem.getId(),
                problem.getQuestion(),
                problemOptionsMap.getOrDefault(problem.getId(), Collections.emptyList()),
                examProblem.getMemberCheckedAnswer(),
                problem.getAnswer(),
                examProblem.getAnswerStatus(),
                bookmarkStatusMap.getOrDefault(problem.getId(), false),
                problem.getExplanation(),
                problem.getCorrectRate(),
                problem.getSubject().name(),
                problem.getProblemType().name()
        );
    }

}