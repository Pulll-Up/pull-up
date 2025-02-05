package com.pullup.interview.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ModifyCommentRequest(
        @Schema(description = "많이 배워갑니다.")
        @NotBlank(message = "댓글 내용은 필수입니다.")
        String content
) {
}
