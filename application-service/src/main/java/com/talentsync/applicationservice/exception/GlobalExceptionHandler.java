package com.talentsync.applicationservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApplicationNotFoundException.class)
    ResponseEntity<?> notFound(ApplicationNotFoundException ex, HttpServletRequest request) { return error(HttpStatus.NOT_FOUND, ex.getMessage(), request); }
    @ExceptionHandler({DuplicateApplicationException.class, JobUnavailableException.class})
    ResponseEntity<?> conflict(RuntimeException ex, HttpServletRequest request) { return error(HttpStatus.CONFLICT, ex.getMessage(), request); }
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<?> invalidFile(IllegalArgumentException ex, HttpServletRequest request) { return error(HttpStatus.BAD_REQUEST, ex.getMessage(), request); }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> invalid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fields = ex.getBindingResult().getFieldErrors().stream().collect(java.util.stream.Collectors.toMap(x -> x.getField(), x -> x.getDefaultMessage(), (a, b) -> a));
        return ResponseEntity.badRequest().body(Map.of("timestamp", Instant.now(), "status", 400, "error", "Validation failed", "fields", fields, "path", request.getRequestURI()));
    }
    private ResponseEntity<?> error(HttpStatus status, String message, HttpServletRequest request) { return ResponseEntity.status(status).body(Map.of("timestamp", Instant.now(), "status", status.value(), "error", message, "path", request.getRequestURI())); }
}
