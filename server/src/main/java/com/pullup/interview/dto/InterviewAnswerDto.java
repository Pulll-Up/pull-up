package com.pullup.interview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record InterviewAnswerDto(
        @Schema(description = "인터뷰 ID", example = "2")
        Long interviewId,
        @Schema(description = "작성자 이름", example = "이석환")
        String memberName,
        @Schema(description = "작성 날짜", example = "2025-01-31T00:00:00")
        LocalDateTime date,
        @Schema(description = "답변", example = "캐시 메모리는 CPU와 메인 메모리 사이에 위치하여 자주 사용되는 데이터를 빠르게 제공하여 성능을 향상시킨다.")
        String answer,
        @Schema(description = "좋아요 여부", example = "true")
        Boolean isLiked,
        @Schema(description = "좋아요 수", example = "10")
        Integer likeCount,
        @Schema(description = "댓글 수", example = "5")
        Integer commentCount
) {
    public static InterviewAnswerDto of(Long interviewId, String memberName, LocalDateTime date, String answer, Boolean isLiked, Integer likeCount, Integer commentCount) {
        return InterviewAnswerDto.builder()
                .interviewId(interviewId)
                .memberName(memberName)
                .date(date)
                .answer(answer)
                .isLiked(isLiked)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .build();
    }
}