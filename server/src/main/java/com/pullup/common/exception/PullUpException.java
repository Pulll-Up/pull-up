package com.pullup.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PullUpException extends RuntimeException {
    private final HttpStatus status;
    private final ErrorMessage errorMessage;

    public PullUpException(HttpStatus status, ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
