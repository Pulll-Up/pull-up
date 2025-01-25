package com.pullup.exam.dto;

import com.pullup.exam.domain.Exam;
import lombok.Builder;

public record ExamScoreDto(
        Integer round,
        Integer score
) {
    @Builder
    public ExamScoreDto {
    }

    public static ExamScoreDto of(Exam exam) {
        return ExamScoreDto.builder()
                .round(exam.getRound())
                .score(exam.getScore())
                .build();
    }
}
