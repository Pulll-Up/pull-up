package com.pullup.auth.jwt.exception;

import com.pullup.auth.jwt.TokenType;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException(JwtExceptionMessage jwtExceptionMessage, TokenType tokenType) {
        super(String.format("[%s] %s", tokenType.name(), jwtExceptionMessage.getMessage()));
    }
}