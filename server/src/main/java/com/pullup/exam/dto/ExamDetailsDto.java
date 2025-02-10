package com.pullup.exam.dto;

import com.pullup.problem.domain.ProblemType;
import java.util.List;
import lombok.Builder;

@Builder
public record ExamDetailsDto(
        Long problemId,
        String problem,
        List<String> options,
        String subject,
        ProblemType problemType
) {
    public static ExamDetailsDto of(
            Long problemId,
            String problem,
            List<String> options,
            String subject,
            ProblemType problemType
    ) {
        return ExamDetailsDto.builder()
                .problemId(problemId)
                .problem(problem)
                .options(options)
                .subject(subject)
                .problemType(problemType)
                .build();
    }

}
