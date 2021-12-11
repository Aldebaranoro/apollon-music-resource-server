package com.github.aldebaranoro.apollonmusicresourceserver.exception;

import com.github.aldebaranoro.apollonmusicresourceserver.exception.dto.ForbiddenException;
import com.github.aldebaranoro.apollonmusicresourceserver.exception.dto.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;

@RestControllerAdvice
class ControllerExceptionHandler {

    private ResponseEntity<ExceptionView> error(Exception ex, HttpStatus httpStatus, HttpServletRequest request) {
        var exceptionView = new ExceptionView(httpStatus, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(exceptionView, httpStatus);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ValidateExceptionView> validateException(BindException ex, HttpServletRequest request) {
        var httpStatus = HttpStatus.BAD_REQUEST;
        var exceptionView = new ValidateExceptionView(
                httpStatus,
                "Validation failed",
                request.getRequestURI(),
                ex.getBindingResult().getFieldErrors()
        );
        return new ResponseEntity<>(exceptionView, httpStatus);
    }

    @ExceptionHandler({
            InvalidParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ExceptionView> invalidParameterException(Exception ex, HttpServletRequest request) {
        return error(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionView> forbiddenExceptionHandler(Exception ex, HttpServletRequest request) {
        return error(ex, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionView> resourceNotFoundExceptionHandler(Exception ex, HttpServletRequest request) {
        return error(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionView> globalExceptionHandler(Exception ex, HttpServletRequest request) {
        return error(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
