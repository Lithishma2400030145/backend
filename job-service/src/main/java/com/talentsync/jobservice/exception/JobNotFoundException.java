package com.talentsync.jobservice.exception;

/**
 * Exception thrown when a job is not found
 */
public class JobNotFoundException extends RuntimeException {

    public JobNotFoundException(String message) {
        super(message);
    }

    public JobNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static JobNotFoundException forId(Long id) {
        return new JobNotFoundException("Job with id " + id + " not found");
    }
}
