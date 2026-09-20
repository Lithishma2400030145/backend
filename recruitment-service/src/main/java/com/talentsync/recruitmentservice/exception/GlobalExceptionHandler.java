package com.talentsync.recruitmentservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RecruitmentExceptions.NotFound.class) ResponseEntity<?> notFound(RuntimeException ex, HttpServletRequest req) { return body(HttpStatus.NOT_FOUND, ex.getMessage(), req); }
    @ExceptionHandler(RecruitmentExceptions.Duplicate.class) ResponseEntity<?> duplicate(RuntimeException ex, HttpServletRequest req) { return body(HttpStatus.CONFLICT, ex.getMessage(), req); }
    @ExceptionHandler(IllegalArgumentException.class) ResponseEntity<?> invalid(RuntimeException ex, HttpServletRequest req) { return body(HttpStatus.BAD_REQUEST, ex.getMessage(), req); }
    @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<?> validation(MethodArgumentNotValidException ex, HttpServletRequest req) { Map<String, String> fields = new java.util.LinkedHashMap<>(); ex.getBindingResult().getFieldErrors().forEach(error -> fields.put(error.getField(), error.getDefaultMessage())); return ResponseEntity.badRequest().body(Map.of("timestamp", Instant.now(), "status", 400, "error", "Validation failed", "fields", fields, "path", req.getRequestURI())); }
    private ResponseEntity<?> body(HttpStatus s, String message, HttpServletRequest req) { return ResponseEntity.status(s).body(Map.of("timestamp", Instant.now(), "status", s.value(), "error", message, "path", req.getRequestURI())); }
}
