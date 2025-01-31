package com.pullup.interview.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record InterviewAnswerRequest(
        @Schema(description = "유저 답변", example = "캐시 메모리는 CPU와 메인 메모리 사이에 위치하여 자주 사용되는 데이터를 빠르게 제공하여 성능을 향상시킨다.")
        @NotBlank(message = "답변은 필수입니다.")
        String answer
) {
}