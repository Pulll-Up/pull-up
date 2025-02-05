package com.pullup.interview.dto.response;

import com.pullup.interview.dto.InterviewAnswerDto;
import io.swagger.v3.oas.annotations.media.Schema;

public record InterviewAnswerWithCommentsResponse(
        @Schema(description = "오늘의 문제 답변")
        InterviewAnswerDto interviewAnswerDto,
        @Schema(description = "오늘의 문제 답변에 대한 댓글")
        CommentsResponse commentsResponse
) {
    public static InterviewAnswerWithCommentsResponse of(InterviewAnswerDto interviewAnswerDto, CommentsResponse commentsResponse) {
        return new InterviewAnswerWithCommentsResponse(interviewAnswerDto, commentsResponse);
    }
}
