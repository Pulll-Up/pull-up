package com.pullup.interview.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;

public record ModifyCommentResponse(
        @NotBlank(message = "댓글 내용은 필수입니다.")
        String content
) {
}
