package com.pullup.interview.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MyInterviewAnswerResponse(
        @Schema(description = "오늘의 문제 ID", example = "1")
        Long interviewId,
        @Schema(description = "오늘의 문제 답변 ID", example = "1")
        Long interviewAnswerId
) {

    public static MyInterviewAnswerResponse of(Long interviewId, Long interviewAnswerId) {
        return new MyInterviewAnswerResponse(interviewId, interviewAnswerId);
    }
}
