package com.concalldrift.exception;

import com.concalldrift.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleBadCredentials(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Invalid username or password"));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<?>> handleDisabled(DisabledException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Account is disabled"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneral(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An unexpected error occurred"));
    }
}
