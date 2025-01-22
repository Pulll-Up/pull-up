package com.pullup.common.exception;

import org.springframework.http.HttpStatus;

public class IllegalArgumentException extends PullUpException {

    public IllegalArgumentException(ErrorMessage errorMessage) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
    }
}
