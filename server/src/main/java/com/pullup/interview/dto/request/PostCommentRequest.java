package com.pullup.interview.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PostCommentRequest(
        @Schema(description = "덕분에 많은 도움이 되었습니다.")
        @NotBlank(message = "댓글 내용은 필수입니다.")
        String content
) {
}