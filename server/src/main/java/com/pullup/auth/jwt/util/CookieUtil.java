package com.pullup.auth.jwt.util;

import static com.pullup.auth.jwt.config.JwtConstants.ACCESS_TOKEN_COOKIE_NAME;
import static com.pullup.auth.jwt.config.JwtConstants.REFRESH_TOKEN_COOKIE_NAME;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.http.ResponseCookie;

public class CookieUtil {

    public static ResponseCookie createAccessTokenForCookie(String accessToken) {
        return ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, accessToken)
                .secure(true)
                .path("/")
                .maxAge(60 * 10)
                .sameSite("None") // 기존에는 "Strict"
                .build();
    }

    public static ResponseCookie createRefreshTokenForCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24 * 7)
                .sameSite("None") // 기존에는 "Strict"
                .build();
    }

    public static ResponseCookie createDeleteTokenAtCookie(String token) {
        return ResponseCookie.from(token, "")
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None") // 기존에는 "Strict"
                .build();
    }

    public static Optional<String> extractTokenFromCookie(HttpServletRequest request, String tokenName) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(tokenName)) {
                token = cookie.getValue();
                break;
            }
        }
        return Optional.ofNullable(token);
    }
}
