package com.pullup.auth.jwt.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtExceptionMessage {
    /*
    Token 관련 Exception
     */
    ERR_INVALID_JWT_SIGNATURE("유효하지 않은 Signature 입니다."),
    ERR_INVALID_JWT_TOKEN("유효하지 않은 Token 입니다."),
    ERR_EXPIRED_JWT_TOKEN("만료된 Token 입니다."),
    ERR_UNSUPPORTED_JWT_TOKEN("지원하지 않는 형식의 Token 입니다."),
    ERR_EMPTY_JWT("토큰이 비어있습니다."),

    ERR_NOT_EXISTS_JWT("토큰이 존재하지 않습니다."),

    /*
    Authorization 관련 Exception
     */
    ERR_NOT_AUTHENTICATED_MEMBER("인증되지 않은 사용자입니다."),
    ;
    private final String message;
}