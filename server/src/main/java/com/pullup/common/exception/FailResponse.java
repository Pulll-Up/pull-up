package com.pullup.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
@Builder
public class FailResponse {

    private final int status;

    private final String errorMessage;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> errors;

    public static FailResponse fail(final int status, final String errorMessage) {
        return FailResponse.builder()
                .status(status)
                .errorMessage(errorMessage)
                .build();
    }

    public static FailResponse failFromMethodArgumentNotValid(final int status, final List<ValidationError> errors) {
        return FailResponse.builder()
                .status(status)
                .errors(errors)
                .build();
    }

    @Builder
    public record ValidationError(
            String field,
            String message
    ) {

        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }
}
