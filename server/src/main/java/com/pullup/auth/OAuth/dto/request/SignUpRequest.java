package com.pullup.auth.OAuth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record SignUpRequest(
        @Schema(description = "선호 과목", example = "[OS, NETWORK]")
        List<String> subjectNames
) {
}