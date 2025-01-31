package com.pullup.interview.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
public record InterviewResponse(
        @Schema(description = "문제 ID", example = "1")
        Long interviewId,
        @Schema(description = "문제", example = "멀티스레딩과 멀티프로세싱의 차이는")
        String question,
        @Schema(description = "키워드", example = "[멀티스레딩, 멀티프로세싱]")
        List<String> keywords
) {

    public static InterviewResponse of(
            Long interviewId,
            String question,
            List<String> keywords
    ) {
        return InterviewResponse.builder()
                .interviewId(interviewId)
                .question(question)
                .keywords(keywords)
                .build();
    }
}
