package com.pullup.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends PullUpException {
    public NotFoundException(ErrorMessage errorMessage) {
        super(HttpStatus.NOT_FOUND, errorMessage);
    }
}
