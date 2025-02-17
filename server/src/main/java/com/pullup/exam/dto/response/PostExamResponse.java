package com.pullup.exam.dto.response;

public record PostExamResponse(
        String examId

) {
    public static PostExamResponse of(String examId) {
        return new PostExamResponse(examId);
    }
}
