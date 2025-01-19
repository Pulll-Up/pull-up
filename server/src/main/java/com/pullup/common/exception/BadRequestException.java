package com.pullup.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends PullUpException {

    public BadRequestException(ErrorMessage errorMessage) {
        super(HttpStatus.BAD_REQUEST, errorMessage);
    }
}