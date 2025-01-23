package com.pullup.auth.jwt.util;

import static com.pullup.auth.jwt.config.JwtConstants.REFRESH_TOKEN_COOKIE_NAME;
import static com.pullup.auth.jwt.config.JwtConstants.REFRESH_TOKEN_PREFIX;

import com.pullup.auth.jwt.JwtToken;
import com.pullup.auth.jwt.config.JwtConstants;
import com.pullup.auth.jwt.config.JwtProperties;
import com.pullup.auth.jwt.config.JwtSecretKey;
import com.pullup.common.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtSecretKey jwtSecretKey;
    private final JwtProperties jwtProperties;
    private final RedisUtil redisUtil;

    public void issueAccessTokenAndRefreshToken(Long memberId, HttpServletResponse response) {
        JwtToken jwtToken = generateJwtTokens(memberId);
        issueAccessTokenInHeader(jwtToken.accessToken(), response);
        issueRefreshTokenInCookie(jwtToken.refreshToken(), response);
    }

    public void issueAccessTokenInHeader(String accessToken, HttpServletResponse response) {
        response.setHeader(JwtConstants.AUTHORIZATION_HEADER, JwtConstants.BEARER_PREFIX + accessToken);
    }

    public void issueRefreshTokenInCookie(String refreshToken, HttpServletResponse response) {
        ResponseCookie refreshTokenCookie = CookieUtil.createRefreshTokenForCookie(refreshToken);
        response.addHeader("set-cookie", refreshTokenCookie.toString());
    }

    private JwtToken generateJwtTokens(Long memberId) {
        String accessToken = generateAccessToken(memberId);
        String refreshToken = generateRefreshToken(memberId);
        storeRefreshToken(memberId, refreshToken);

        return new JwtToken(accessToken, refreshToken);
    }

    private String generateAccessToken(Long memberId) {
        return Jwts.builder()
                .claim("id", memberId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration()))
                .signWith(jwtSecretKey.getSecretKey())
                .compact();
    }

    private String generateRefreshToken(Long memberId) {
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

    public Long resolveMemberIdFromJwtToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtSecretKey.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Redis 관련 Method
     */

    private void storeRefreshToken(Long memberId, String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + memberId;
        redisUtil.delete(key);
        redisUtil.setValueWithExpiration(key, refreshToken, jwtProperties.getRefreshTokenExpiration());
    }

    public boolean existsByRefreshToken(String refreshToken) {
        Long memberId = resolveMemberIdFromJwtToken(refreshToken);
        String key = REFRESH_TOKEN_PREFIX + memberId;
        String storedToken = redisUtil.getValue(key);
        return storedToken != null && storedToken.equals(refreshToken);
    }
}
