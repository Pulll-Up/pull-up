package com.pullup.exam.dto;

import com.pullup.exam.domain.ExamProblem;
import com.pullup.problem.domain.Problem;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public record ExamResultDetailDto(
        String problemId,
        String problem,
        Set<String> options,
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
            String problemId,
            Problem problem,
            ExamProblem examProblem,
            Map<Long, Set<String>> problemOptionsMap,
            Map<Long, Boolean> bookmarkStatusMap
    ) {
        return new ExamResultDetailDto(
                problemId,
                problem.getQuestion(),
                problemOptionsMap.getOrDefault(problem.getId(), Collections.emptySet()), // 변경: List 변환 제거
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
