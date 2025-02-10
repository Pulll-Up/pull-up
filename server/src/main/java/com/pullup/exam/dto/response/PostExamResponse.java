package com.pullup.exam.dto.response;

public record PostExamResponse(
        Long examId

) {
    public static PostExamResponse of(Long examId) {
        return new PostExamResponse(examId);
    }
}
