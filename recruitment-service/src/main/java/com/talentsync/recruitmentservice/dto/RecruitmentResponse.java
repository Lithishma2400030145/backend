package com.talentsync.recruitmentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class RecruitmentResponse {
    private Long id; private Long applicationId; private Long candidateId; private Long jobId; private String status;
    private LocalDateTime interviewAt; private LocalDate interviewDate; private LocalTime interviewTime; private String interviewMode; private String meetingLink; private String interviewStatus; private String interviewNotes; private String decisionNotes;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}
