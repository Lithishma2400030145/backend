package com.talentsync.applicationservice.exception;

public class DuplicateApplicationException extends RuntimeException {
    public DuplicateApplicationException(Long candidateId, Long jobId) {
        super("Candidate " + candidateId + " has already applied for job " + jobId);
    }
}
