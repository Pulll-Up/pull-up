package com.pullup.auth.jwt;

import static com.pullup.auth.jwt.exception.JwtExceptionMessage.EMPTY_JWT;
import static com.pullup.auth.jwt.exception.JwtExceptionMessage.EXPIRED_JWT_TOKEN;
import static com.pullup.auth.jwt.exception.JwtExceptionMessage.INVALID_JWT_SIGNATURE;
import static com.pullup.auth.jwt.exception.JwtExceptionMessage.INVALID_JWT_TOKEN;
import static com.pullup.auth.jwt.exception.JwtExceptionMessage.UNSUPPORTED_JWT_TOKEN;

import com.pullup.auth.jwt.config.JwtSecretKey;
import com.pullup.auth.jwt.exception.JwtAuthenticationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenValidator {
    private final JwtSecretKey secretKey;

    public void validateJwtToken(String token, TokenType tokenType) throws JwtAuthenticationException {
        try {
            Jwts.parser()
                    .verifyWith(secretKey.getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
            throw new JwtAuthenticationException(INVALID_JWT_SIGNATURE, tokenType);
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            throw new JwtAuthenticationException(INVALID_JWT_TOKEN, tokenType);
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
            throw new JwtAuthenticationException(EXPIRED_JWT_TOKEN, tokenType);
        } catch (UnsupportedJwtException e) {
            log.warn("JWT token is unsupported: {}", e.getMessage());
            throw new JwtAuthenticationException(UNSUPPORTED_JWT_TOKEN, tokenType);
        } catch (Exception e) {
            log.warn("JWT token validation failed: {}", e.getMessage());
            throw new JwtAuthenticationException(EMPTY_JWT, tokenType);
        }
    }
}
