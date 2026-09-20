package com.talentsync.authservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthExceptions.InvalidCredentials.class) ResponseEntity<?> credentials(RuntimeException ex, HttpServletRequest req) { return error(HttpStatus.UNAUTHORIZED, ex.getMessage(), req); }
    @ExceptionHandler(AuthExceptions.Conflict.class) ResponseEntity<?> conflict(RuntimeException ex, HttpServletRequest req) { return error(HttpStatus.CONFLICT, ex.getMessage(), req); }
    @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<?> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> fields = new java.util.LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> fields.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return ResponseEntity.badRequest().body(Map.of("timestamp", Instant.now(), "status", 400, "error", "Validation failed", "fields", fields, "path", req.getRequestURI()));
    }
    @ExceptionHandler(IllegalArgumentException.class) ResponseEntity<?> invalidArgument(IllegalArgumentException ex, HttpServletRequest req) { return error(HttpStatus.BAD_REQUEST, ex.getMessage(), req); }
    private ResponseEntity<?> error(HttpStatus status, String message, HttpServletRequest req) { return ResponseEntity.status(status).body(Map.of("timestamp", Instant.now(), "status", status.value(), "error", message, "path", req.getRequestURI())); }
}
