package com.pullup.auth.jwt.domain;

public record JwtToken(
        String accessToken,
        String refreshToken
) {
}