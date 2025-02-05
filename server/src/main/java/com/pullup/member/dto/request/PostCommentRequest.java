package com.pullup.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PostCommentRequest(
        @Schema(description = "댓글을 달 게시물 ID", example = "1")
        @NotBlank(message = "Interview Answer는 필수입니다.")
        Long interviewAnswerId,

        @Schema(description = "덕분에 많은 도움이 되었습니다.")
        @NotBlank(message = "댓글 내용은 필수입니다.")
        String content
) {
}