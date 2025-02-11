package com.pullup.interview.dto.response;

import com.pullup.interview.dto.SearchedInterviewQuestionDto;
import java.util.List;

public record SearchedInterviewQuestionsResponse(
        List<SearchedInterviewQuestionDto> searchedInterviewQuestions
) {
    public static SearchedInterviewQuestionsResponse of(
            List<SearchedInterviewQuestionDto> searchedInterviewQuestions
    ) {
        return new SearchedInterviewQuestionsResponse(searchedInterviewQuestions);
    }
}