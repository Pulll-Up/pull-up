package com.pullup.interview.dto.response;

import com.pullup.interview.dto.InterviewAnswerDto;
import java.util.List;

public record InterviewAnswersResponse(
        List<InterviewAnswerDto> interviewAnswers
) {
}
