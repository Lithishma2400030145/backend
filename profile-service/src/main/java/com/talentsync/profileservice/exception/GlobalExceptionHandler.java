package com.talentsync.profileservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProfileExceptions.NotFound.class) ResponseEntity<?> notFound(RuntimeException ex, HttpServletRequest req) { return body(HttpStatus.NOT_FOUND, ex.getMessage(), req); }
    @ExceptionHandler(ProfileExceptions.Conflict.class) ResponseEntity<?> conflict(RuntimeException ex, HttpServletRequest req) { return body(HttpStatus.CONFLICT, ex.getMessage(), req); }
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class}) ResponseEntity<?> invalid(Exception ex, HttpServletRequest req) { return body(HttpStatus.BAD_REQUEST, ex instanceof MethodArgumentNotValidException ? "Validation failed" : ex.getMessage(), req); }
    private ResponseEntity<?> body(HttpStatus status, String error, HttpServletRequest req) { return ResponseEntity.status(status).body(Map.of("timestamp", Instant.now(), "status", status.value(), "error", error, "path", req.getRequestURI())); }
}
