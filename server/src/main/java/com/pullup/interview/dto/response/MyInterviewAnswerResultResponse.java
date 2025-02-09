package com.pullup.interview.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record MyInterviewAnswerResultResponse(
        Long interviewId,
        String question,
        String memberAnswer,
        List<String> keywords,
        LocalDateTime createdAt,
        String strength,
        String weakness,
        String answer
) {
    public static MyInterviewAnswerResultResponse of(
            Long interviewId,
            String question,
            String memberAnswer,
            List<String> keywords,
            LocalDateTime createdAt,
            String strength,
            String weakness,
            String answer
    ) {
        return MyInterviewAnswerResultResponse.builder()
                .interviewId(interviewId)
                .question(question)
                .memberAnswer(memberAnswer)
                .keywords(keywords)
                .createdAt(createdAt)
                .strength(strength)
                .weakness(weakness)
                .answer(answer)
                .build();
    }
}
