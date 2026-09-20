package com.talentsync.jobservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Standard error response format
 */
@Data
@AllArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;
    private String error;
    private LocalDateTime timestamp;
    private String path;

    public ErrorResponse(HttpStatus status, String message, String error, String path) {
        this.status = status.value();
        this.message = message;
        this.error = error;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }
}
