package com.pullup.problem.dto;

import com.pullup.problem.domain.Subject;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record WrongProblemDto(
        String problemId,
        String question,
        Subject subject,
        LocalDateTime date
) {

    public static WrongProblemDto of(String problemId, String question, Subject subject, LocalDateTime date) {
        return WrongProblemDto.builder()
                .problemId(problemId)
                .question(question)
                .subject(subject)
                .date(date)
                .build();
    }
}
