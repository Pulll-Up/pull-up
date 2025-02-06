package com.pullup.common.exception;

import org.springframework.http.HttpStatus;

public class IllegalStatementException extends PullUpException {
    public IllegalStatementException(ErrorMessage errorMessage) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
    }
}
