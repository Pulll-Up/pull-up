package com.pullup.interview.dto;

public record SearchedInterviewQuestionDto(
        String interviewAnswerId,
        String question
) {
    public static SearchedInterviewQuestionDto of(
            String interviewAnswerId,
            String question
    ) {
        return new SearchedInterviewQuestionDto(interviewAnswerId, question);
    }
}
