package com.pullup.interview.dto;

public record SearchedInterviewQuestionDto(
        Long interviewAnswerId,
        String question
) {
    public static SearchedInterviewQuestionDto of(
            Long interviewAnswerId,
            String question
    ) {
        return new SearchedInterviewQuestionDto(interviewAnswerId, question);
    }
}
