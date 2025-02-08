package com.pullup.interview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CommentsDto(
        @Schema(description = "댓글 ID", example = "1")
        Long commentId,
        @Schema(description = "댓글 작성자", example = "이석환")
        String writer,
        @Schema(description = "댓글 작성자 이메일", example = "im2sh@gmail.com")
        String email,
        @Schema(description = "댓글 내용", example = "감사합니다.")
        String content,
        @Schema(description = "댓글 생성 시간", example = "2025-02-08 12:00:00")
        LocalDateTime createdAt
) {

    public static CommentsDto of(
            Long commentId,
            String writer,
            String email,
            String content,
            LocalDateTime createdAt
    ) {
        return CommentsDto.builder()
                .commentId(commentId)
                .writer(writer)
                .email(email)
                .content(content)
                .createdAt(createdAt)
                .build();
    }
}