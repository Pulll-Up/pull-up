package com.pullup.interview.dto.response;

import com.pullup.interview.dto.InterviewAnswerDto;
import io.swagger.v3.oas.annotations.media.Schema;

public record InterviewAnswerResponse(
        @Schema(description = "오늘의 문제 답변")
        InterviewAnswerDto interviewAnswer
) {
    public static InterviewAnswerResponse of(InterviewAnswerDto interviewAnswerDto) {
        return new InterviewAnswerResponse(interviewAnswerDto);
    }
}
