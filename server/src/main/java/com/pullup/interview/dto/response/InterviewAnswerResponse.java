package com.pullup.interview.dto.response;

import com.pullup.interview.dto.CommentsDto;
import com.pullup.interview.dto.InterviewAnswerDto;
import java.util.List;

public record InterviewAnswerResponse(
        InterviewAnswerDto interviewAnswer,
        List<CommentsDto> comments
) {
    public static InterviewAnswerResponse of(InterviewAnswerDto interviewAnswerDto, List<CommentsDto> commentsDto) {
        return new InterviewAnswerResponse(interviewAnswerDto, commentsDto);
    }
}
