package com.pullup.exam.dto;


import com.pullup.problem.domain.ProblemType;
import com.pullup.problem.domain.Subject;

public record ExamDetailsWithoutOptionsDto(
        Long problemId,
        String problem,
        Subject subject,
        ProblemType problemType
) {
}
