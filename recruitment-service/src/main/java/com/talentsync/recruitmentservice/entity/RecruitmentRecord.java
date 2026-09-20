package com.talentsync.recruitmentservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "recruitment_records", uniqueConstraints = @UniqueConstraint(name = "uk_recruitment_application", columnNames = "application_id"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "application_id", nullable = false) private Long applicationId;
    @Column(name = "candidate_id", nullable = false) private Long candidateId;
    @Column(name = "job_id", nullable = false) private Long jobId;
    @Column(nullable = false, length = 30) private String status;
    @Column(name = "interview_at") private LocalDateTime interviewAt;
    @Column(name = "interview_date") private LocalDate interviewDate;
    @Column(name = "interview_time") private LocalTime interviewTime;
    @Column(name = "interview_mode", length = 30) private String interviewMode;
    @Column(name = "meeting_link", length = 500) private String meetingLink;
    @Column(name = "interview_status", length = 30) private String interviewStatus = "NOT_SCHEDULED";
    @Column(name = "interview_notes", columnDefinition = "TEXT") private String interviewNotes;
    @Column(name = "decision_notes", columnDefinition = "TEXT") private String decisionNotes;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;
    @Column(nullable = false) private LocalDateTime updatedAt;
    @PrePersist void createTimestamps() { LocalDateTime now = LocalDateTime.now(); createdAt = now; updatedAt = now; }
    @PreUpdate void updateTimestamp() { updatedAt = LocalDateTime.now(); }
}
