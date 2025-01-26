package com.pullup.auth.jwt.util;

import static com.pullup.auth.jwt.config.JwtConstants.ACCESS_TOKEN_COOKIE_NAME;
import static com.pullup.auth.jwt.config.JwtConstants.REFRESH_TOKEN_COOKIE_NAME;
import static com.pullup.auth.jwt.config.JwtConstants.REFRESH_TOKEN_PREFIX;

import com.pullup.auth.jwt.config.JwtConstants;
import com.pullup.auth.jwt.config.JwtProperties;
import com.pullup.auth.jwt.config.JwtSecretKey;
import com.pullup.auth.jwt.domain.JwtToken;
import com.pullup.common.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
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

    public JwtToken generateJwtTokens(Long memberId) {
        String accessToken = generateAccessToken(memberId);
        String refreshToken = generateRefreshToken(memberId);
        storeRefreshTokenInRedis(memberId, refreshToken);

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

    public void clearAuthenticationAndCookies(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = resolveRefreshTokenFromCookie(request);
        Long memberId = resolveMemberIdFromJwtToken(refreshToken);
        redisUtil.delete(JwtConstants.REFRESH_TOKEN_PREFIX + memberId);

        response.addHeader("set-cookie", CookieUtil.createDeleteTokenAtCookie(REFRESH_TOKEN_COOKIE_NAME).toString());
        SecurityContextHolder.clearContext();
    }

    public void extractAccessTokenFromCookieAndIssueAccessTokenInHeader(String accessToken,
                                                                        HttpServletResponse response) {
        ResponseCookie deletedAccessTokenCookie = CookieUtil.createDeleteTokenAtCookie(ACCESS_TOKEN_COOKIE_NAME);
        response.addHeader("set-cookie", deletedAccessTokenCookie.toString());
        issueAccessTokenInHeader(accessToken, response);
    }

    /**
     * Redis 관련 Method
     */

    private void storeRefreshTokenInRedis(Long memberId, String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + memberId;
        redisUtil.delete(key);
        redisUtil.setValueWithExpiration(key, refreshToken, jwtProperties.getRefreshTokenExpiration());
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        Long memberId = resolveMemberIdFromJwtToken(refreshToken);
        String key = REFRESH_TOKEN_PREFIX + memberId;
        String storedToken = redisUtil.getValue(key);

        if (storedToken == null || !storedToken.equals(refreshToken)) {
            handleInvalidRefreshToken(memberId, storedToken);
            return false;
        }

        return true;
    }

    private void handleInvalidRefreshToken(Long memberId, String storedToken) {
        if (storedToken != null) {
            deleteRefreshTokenInRedis(memberId);
        }
    }

    private void deleteRefreshTokenInRedis(Long memberId) {
        redisUtil.delete(JwtConstants.REFRESH_TOKEN_PREFIX + memberId);
    }
}
