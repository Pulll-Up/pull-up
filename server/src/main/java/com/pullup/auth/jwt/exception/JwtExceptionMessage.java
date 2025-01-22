package com.pullup.auth.jwt.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtExceptionMessage {
    /*
    Token 관련 Exception
     */
    INVALID_JWT_SIGNATURE("유효하지 않은 Signature 입니다."),
    INVALID_JWT_TOKEN("유효하지 않은 Token 입니다."),
    EXPIRED_JWT_TOKEN("만료된 Token 입니다."),
    UNSUPPORTED_JWT_TOKEN("지원하지 않는 형식의 Token 입니다."),
    EMPTY_JWT("토큰이 비어있습니다.");

    private final String message;
}