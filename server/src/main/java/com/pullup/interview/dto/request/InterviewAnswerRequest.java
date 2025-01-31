package com.pullup.interview.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record InterviewAnswerRequest(
        @Schema(description = "유저 답변", example = "답변")
        @NotBlank(message = "답변은 필수입니다.")
        String answer
) {
}