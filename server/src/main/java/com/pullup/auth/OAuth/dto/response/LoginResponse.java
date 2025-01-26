package com.pullup.auth.OAuth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 시에 Response")
public record LoginResponse(
        @Schema(description = "계정 존재 여부", example = "false")
        boolean isSignedUp,
        @Schema(description = "오늘 문제 풀이 여부", example = "false")
        boolean isSolvedToday
) {

    public static LoginResponse isFirstLogin() {
        return new LoginResponse(false, false);
    }

    public static LoginResponse isNotFirstLoginAndNotSolvedToday() {
        return new LoginResponse(true, false);
    }

    public static LoginResponse isNotFirstLoginAndSolvedToday() {
        return new LoginResponse(true, true);
    }
}