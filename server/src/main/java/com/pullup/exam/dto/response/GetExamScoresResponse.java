package com.pullup.exam.dto.response;

import com.pullup.exam.dto.ExamScoreDto;
import java.util.List;
import lombok.Builder;

@Builder
public record GetExamScoresResponse(
        List<ExamScoreDto> examScoreDtos
) {
    public static GetExamScoresResponse of(List<ExamScoreDto> examScoreDtos) {
        return GetExamScoresResponse.builder()
                .examScoreDtos(examScoreDtos)
                .build();
    }
}
