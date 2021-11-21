package com.github.aldebaranoro.apollonmusicresourceserver.exception;

import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Value
class ExceptionView {
    LocalDateTime timestamp;
    int status;
    String error;
    String message;
    String path;

    public ExceptionView(HttpStatus httpStatus, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
        this.path = path;
    }
}
