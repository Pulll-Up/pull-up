package com.pullup.interview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record MyInterviewAnswerDto(
        @Schema(description = "오늘의 문제 ID", example = "1")
        String interviewId,
        @Schema(description = "오늘의 문제 답변 ID", example = "1")
        String interviewAnswerId,
        @Schema(description = "오늘의 문제", example = "TCP와 UDP의 차이는?")
        String question
) {
    public static MyInterviewAnswerDto of(String interviewId, String interviewAnswerId, String question) {
        return MyInterviewAnswerDto.builder()
                .interviewId(interviewId)
                .interviewAnswerId(interviewAnswerId)
                .question(question)
                .build();
    }
}