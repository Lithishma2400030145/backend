package com.talentsync.jobservice.exception;

/**
 * Exception thrown for invalid job data
 */
public class InvalidJobDataException extends RuntimeException {

    public InvalidJobDataException(String message) {
        super(message);
    }

    public InvalidJobDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
