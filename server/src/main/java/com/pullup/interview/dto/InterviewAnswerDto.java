package com.pullup.interview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record InterviewAnswerDto(
        @Schema(description = "오늘의 문제에 대한 답변 ID", example = "2")
        String interviewAnswerId,
        @Schema(description = "문제 제목", example = "캐시 메모리는 어떻게 동작하나요?")
        String question,
        @Schema(description = "키워드", example = "캐시 메모리, 동작")
        List<String> keywords,
        @Schema(description = "작성자 이름", example = "이석환")
        String memberName,
        @Schema(description = "답변", example = "캐시 메모리는 CPU와 메인 메모리 사이에 위치하여 자주 사용되는 데이터를 빠르게 제공하여 성능을 향상시킨다.")
        String answer,
        @Schema(description = "작성 날짜", example = "2025-02-08 12:00:00")
        LocalDateTime createdAt,
        @Schema(description = "좋아요 여부", example = "true")
        Boolean isLiked,
        @Schema(description = "좋아요 수", example = "10")
        Long likeCount,
        @Schema(description = "댓글 수", example = "5")
        Integer commentCount
) {
    public static InterviewAnswerDto of(
            String interviewAnswerId,
            String question,
            List<String> keywords,
            String memberName,
            String answer,
            LocalDateTime createdAt,
            Boolean isLiked,
            Long likeCount,
            Integer commentCount
    ) {
        return InterviewAnswerDto.builder()
                .interviewAnswerId(interviewAnswerId)
                .question(question)
                .keywords(keywords)
                .memberName(memberName)
                .createdAt(createdAt)
                .answer(answer)
                .isLiked(isLiked)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .build();
    }
}