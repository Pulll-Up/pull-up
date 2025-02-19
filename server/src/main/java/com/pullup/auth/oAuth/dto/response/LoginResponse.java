package com.pullup.auth.oAuth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 시에 Response")
public record LoginResponse(
        @Schema(description = "이메일", example = "email@email.com")
        String email,
        @Schema(description = "계정 존재 여부", example = "false")
        boolean isSignedUp,
        @Schema(description = "오늘 문제 풀이 여부", example = "false")
        boolean isSolvedToday,
        @Schema(description = "문제를 푼 경우, 정답 번호를 반환", example = "1")
        String interviewAnswerId
) {

    public static LoginResponse isFirstLogin(String email) {
        return new LoginResponse(email, false, false, null);
    }

    public static LoginResponse isNotFirstLoginAndNotSolvedToday(String email) {
        return new LoginResponse(email, true, false, null);
    }

    public static LoginResponse isNotFirstLoginAndSolvedToday(String email, String interviewAnswerId) {
        return new LoginResponse(email, true, true, interviewAnswerId);
    }
}