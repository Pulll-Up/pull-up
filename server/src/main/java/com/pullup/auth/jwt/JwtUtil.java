package com.pullup.auth.jwt;

import com.pullup.auth.jwt.config.JwtProperties;
import com.pullup.auth.jwt.config.JwtSecretKey;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtSecretKey jwtSecretKey;
    private final JwtProperties jwtProperties;

    public JwtToken generateJwtTokens(
            Long memberId,
            String role
    ) {
        String accessToken = generateAccessToken(memberId, role);
        String refreshToken = generateRefreshToken();

        return new JwtToken(accessToken, refreshToken);
    }

    private String generateAccessToken(
            Long memberId,
            String role
    ) {
        return Jwts.builder()
                .claim("id", memberId)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration()))
                .signWith(jwtSecretKey.getSecretKey())
                .compact();
    }

    private String generateRefreshToken() {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenExpiration()))
                .signWith(jwtSecretKey.getSecretKey())
                .compact();
    }
}
