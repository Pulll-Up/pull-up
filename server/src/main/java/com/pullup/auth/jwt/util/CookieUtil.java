package com.pullup.auth.jwt.util;

import static com.pullup.auth.jwt.config.JwtConstants.ACCESS_TOKEN_COOKIE_NAME;
import static com.pullup.auth.jwt.config.JwtConstants.REFRESH_TOKEN_COOKIE_NAME;

import org.springframework.http.ResponseCookie;

public class CookieUtil {

    public static ResponseCookie createAccessTokenForCookie(String accessToken) {
        return ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, accessToken)
                .secure(true)
                .path("/")
                .maxAge(60 * 10)
                .sameSite("Strict")
                .build();
    }

    public static ResponseCookie createRefreshTokenForCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24 * 7)
                .sameSite("Strict")
                .build();
    }

    public static ResponseCookie deleteTokenAtCookie(String token) {
        return ResponseCookie.from(token, "")
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }
}
