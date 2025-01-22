package com.pullup.auth.jwt.util;

import static com.pullup.auth.jwt.config.JwtConstants.REFRESH_TOKEN_COOKIE_NAME;

import com.pullup.auth.jwt.JwtToken;
import com.pullup.auth.jwt.config.JwtConstants;
import com.pullup.auth.jwt.config.JwtProperties;
import com.pullup.auth.jwt.config.JwtSecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
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
        String refreshToken = generateRefreshToken(memberId);

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

    private String generateRefreshToken(
            Long memberId
    ) {
        return Jwts.builder()
                .claim("id", memberId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenExpiration()))
                .signWith(jwtSecretKey.getSecretKey())
                .compact();
    }

    public String resolveAccessTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtConstants.AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(JwtConstants.BEARER_PREFIX)) {
            return bearerToken.split(" ")[1];
        }
        return null;
    }

    public String resolveRefreshTokenFromCookie(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .findFirst()
                .toString();
    }

    public Long resolveMemberIdFromAccessToken(String accessToken) {
        Claims claims = getClaims(accessToken);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtSecretKey.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
