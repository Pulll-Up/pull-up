package com.pullup.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record DeviceTokenRequest(
        @Schema(description = "디바이스 토큰", example = "bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1")
        @NotBlank(message = "deviceToken은 null일 수 없습니다.")
        String deviceToken
) {
}