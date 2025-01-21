package com.pullup.auth.jwt;

public record JwtToken(
        String accessToken,
        String refreshToken
) {
}