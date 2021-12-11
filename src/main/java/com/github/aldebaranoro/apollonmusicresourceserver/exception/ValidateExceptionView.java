package com.github.aldebaranoro.apollonmusicresourceserver.exception;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Value
class ValidateExceptionView {
    LocalDateTime timestamp;
    int status;
    String error;
    String message;
    List<ValidateFieldResponse> errors;
    String path;

    public ValidateExceptionView(HttpStatus httpStatus, String message, String path, List<FieldError> errors) {
        this.timestamp = LocalDateTime.now();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
        this.path = path;
        this.errors = errors.stream()
                .map(error -> new ValidateFieldResponse(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    record ValidateFieldResponse(String fieldName, String message) {
    }
}
