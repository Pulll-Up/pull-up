package com.pullup.interview.dto.response;

import com.pullup.interview.dto.CommentsDto;
import com.pullup.interview.dto.InterviewAnswerDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record InterviewAnswerWithCommentsResponse(
        @Schema(description = "오늘의 문제 답변")
        InterviewAnswerDto interviewAnswer,
        @Schema(description = "오늘의 문제 답변에 대한 댓글")
        List<CommentsDto> comments
) {
    public static InterviewAnswerWithCommentsResponse of(InterviewAnswerDto interviewAnswerDto, List<CommentsDto> commentsDto) {
        return new InterviewAnswerWithCommentsResponse(interviewAnswerDto, commentsDto);
    }
}
