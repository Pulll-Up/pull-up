package com.pullup.interview.dto.response;

import com.pullup.interview.dto.MyInterviewAnswerDto;
import java.util.List;

public record MyInterviewAnswersResponse(
        List<MyInterviewAnswerDto> myInterviewAnswers
) {
    public static MyInterviewAnswersResponse of(List<MyInterviewAnswerDto> myInterviewAnswers) {
        return new MyInterviewAnswersResponse(myInterviewAnswers);
    }
}