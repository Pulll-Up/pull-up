package com.pullup.exam.dto;

import com.pullup.problem.domain.ProblemType;
import java.util.Set;
import lombok.Builder;

@Builder
public record ExamDetailsDto(
        String problemId,
        String problem,
        Set<String> options,
        String subject,
        ProblemType problemType,
        Boolean bookmarkStatus
) {
    public static ExamDetailsDto of(
            String problemId,
            String problem,
            Set<String> options,
            String subject,
            ProblemType problemType,
            Boolean bookmarkStatus
    ) {
        return ExamDetailsDto.builder()
                .problemId(problemId)
                .problem(problem)
                .options(options)
                .subject(subject)
                .problemType(problemType)
                .bookmarkStatus(bookmarkStatus)
                .build();
    }

}
