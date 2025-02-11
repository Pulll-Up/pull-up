package com.pullup.common.handler;


import com.pullup.common.exception.FailResponse;
import com.pullup.common.exception.FailResponse.ValidationError;
import com.pullup.common.exception.PullUpException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PullUpException.class)
    public ResponseEntity<FailResponse> handleGlobalException(PullUpException exception) {
        log.warn("[PullUpException] {}: {}", exception.getClass().getName(), exception.getErrorMessage());

        return ResponseEntity.status(exception.getStatus())
                .body(FailResponse.fail(exception.getStatus().value(), exception.getErrorMessage().getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request
    ) {
        log.warn("[MethodArgumentNotValidException] {}: {}", ex.getClass().getName(), ex.getMessage());

        BindingResult bindingResult = ex.getBindingResult();
        List<ValidationError> validationErrors = bindingResult.getFieldErrors()
                .stream()
                .map(ValidationError::of)
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(FailResponse.failFromMethodArgumentNotValid(HttpStatus.BAD_REQUEST.value(), validationErrors));
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(
            NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request
    ) {
        log.warn("[NoResourceFoundException] {}: {}", ex.getClass().getName(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(FailResponse.fail(HttpStatus.NOT_FOUND.value(), "잘못된 Resource 요청입니다."));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<FailResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        log.warn("[ConstraintViolationException] {}: {}", exception.getClass().getName(), exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(FailResponse.fail(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }
}
