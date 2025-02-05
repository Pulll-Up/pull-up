package com.pullup.interview.dto.response;

import com.pullup.interview.dto.InterviewAnswerDto;

public record InterviewAnswerWithCommentsResponse(
        InterviewAnswerDto interviewAnswerDto,
        CommentsResponse commentsResponse
) {
    public static InterviewAnswerWithCommentsResponse of(InterviewAnswerDto interviewAnswerDto, CommentsResponse commentsResponse) {
        return new InterviewAnswerWithCommentsResponse(interviewAnswerDto, commentsResponse);
    }
}
