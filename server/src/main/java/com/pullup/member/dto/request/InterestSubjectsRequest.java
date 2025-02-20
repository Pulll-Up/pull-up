package com.pullup.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record InterestSubjectsRequest(
        @Schema(description = "선호 과목", example = "[OS, NETWORK]")
        @NotNull(message = "과목 리스트는 null일 수 없습니다.")
        @Size(min = 1, message = "최소 한 개 이상의 과목을 입력해야 합니다.")
        List<String> subjectNames
) {
}
