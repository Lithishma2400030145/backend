package com.talentsync.applicationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApplicationResponse {
    private Long id;
    private Long candidateId;
    private Long jobId;
    private String status;
    private String coverLetter;
    private String resumeUrl;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}
