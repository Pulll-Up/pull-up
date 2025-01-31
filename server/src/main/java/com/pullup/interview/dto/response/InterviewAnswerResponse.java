package com.pullup.interview.dto.response;

public record InterviewAnswerResponse(
        Long interviewId,
        Long interviewAnswerId
) {

    public static InterviewAnswerResponse of(
            Long interviewId,
            Long interviewAnswerId
    ) {
        return new InterviewAnswerResponse(interviewId, interviewAnswerId);
    }
}
