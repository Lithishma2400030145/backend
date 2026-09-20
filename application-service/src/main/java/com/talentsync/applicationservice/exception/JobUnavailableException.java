package com.talentsync.applicationservice.exception;

public class JobUnavailableException extends RuntimeException {
    public JobUnavailableException(Long jobId) {
        super("Job " + jobId + " does not exist or is not available");
    }
}
