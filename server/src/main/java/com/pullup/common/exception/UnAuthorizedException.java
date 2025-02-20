package com.pullup.common.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends PullUpException {

    public UnAuthorizedException(ErrorMessage errorMessage) {
        super(HttpStatus.UNAUTHORIZED, errorMessage);
    }
}
