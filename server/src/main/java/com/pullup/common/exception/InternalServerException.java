package com.pullup.common.exception;

import org.springframework.http.HttpStatus;

public class InternalServerException extends PullUpException {

    public InternalServerException(ErrorMessage errorMessage) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
    }
}
