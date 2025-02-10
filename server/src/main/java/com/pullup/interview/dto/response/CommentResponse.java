package com.pullup.interview.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CommentResponse(
        @Schema(description = "댓글 ID", example = "1")
        Long commentId,
        @Schema(description = "댓글 작성자", example = "이석환")
        String writer,
        @Schema(description = "댓글 작성자 이메일", example = "email@email.com")
        String email,
        @Schema(description = "댓글 내용", example = "덕분에 쉽게 이해했습니다 !")
        String content
) {
    public static CommentResponse of(
            Long commentId,
            String writer,
            String email,
            String content
    ) {
        return CommentResponse.builder()
                .commentId(commentId)
                .writer(writer)
                .email(email)
                .content(content)
                .build();
    }
}