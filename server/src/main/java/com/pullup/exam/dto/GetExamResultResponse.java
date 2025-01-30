package com.pullup.exam.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record GetExamResultResponse(
        String round,
        Integer score,
        List<ExamResultDetailDto> examResultDetailDtos
) {
    public static GetExamResultResponse of(String round, Integer score,
                                           List<ExamResultDetailDto> examResultDetailDtos) {
        return GetExamResultResponse.builder()
                .round(round)
                .score(score)
                .examResultDetailDtos(examResultDetailDtos)
                .build();
    }
}