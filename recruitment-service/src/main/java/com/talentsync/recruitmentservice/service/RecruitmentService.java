package com.talentsync.recruitmentservice.service;

import com.talentsync.recruitmentservice.dto.*;
import com.talentsync.recruitmentservice.entity.RecruitmentRecord;
import com.talentsync.recruitmentservice.exception.RecruitmentExceptions;
import com.talentsync.recruitmentservice.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruitmentService {
    private final RecruitmentRepository repository;

    public RecruitmentResponse create(RecruitmentRequest request) {
        if (repository.findByApplicationId(request.getApplicationId()).isPresent()) throw new RecruitmentExceptions.Duplicate(request.getApplicationId());
        RecruitmentRecord record = new RecruitmentRecord(); record.setApplicationId(request.getApplicationId()); record.setCandidateId(request.getCandidateId()); record.setJobId(request.getJobId()); record.setStatus("IN_REVIEW"); record.setInterviewStatus("NOT_SCHEDULED");
        return toResponse(repository.save(record));
    }
    @Transactional(readOnly = true) public RecruitmentResponse get(Long id) { return repository.findById(id).map(this::toResponse).orElseThrow(() -> new RecruitmentExceptions.NotFound(id)); }
    @Transactional(readOnly = true) public RecruitmentResponse byApplication(Long applicationId) { return repository.findByApplicationId(applicationId).map(this::toResponse).orElseThrow(() -> new RecruitmentExceptions.NotFound(applicationId)); }
    @Transactional(readOnly = true) public List<RecruitmentResponse> byCandidate(Long id) { return repository.findByCandidateId(id).stream().map(this::toResponse).toList(); }
    @Transactional(readOnly = true) public List<RecruitmentResponse> byJob(Long id) { return repository.findByJobId(id).stream().map(this::toResponse).toList(); }
    @Transactional(readOnly = true) public List<RecruitmentResponse> byStatus(String status) { return repository.findByStatus(status.toUpperCase()).stream().map(this::toResponse).toList(); }
    public RecruitmentResponse updateStatus(Long id, StatusRequest request) { RecruitmentRecord r = require(id); String status = request.getStatus().trim().toUpperCase(); if ("COMPLETED".equals(status)) { if (!"SCHEDULED".equals(r.getInterviewStatus())) throw new IllegalArgumentException("Interview must be scheduled before it can be completed"); r.setInterviewStatus("COMPLETED"); } r.setStatus(status); r.setDecisionNotes(request.getNotes()); return toResponse(repository.save(r)); }
    public RecruitmentResponse schedule(Long id, InterviewRequest request) { RecruitmentRecord r = require(id); if (!"SHORTLISTED".equals(r.getStatus())) throw new IllegalArgumentException("Interview can only be scheduled for a SHORTLISTED application"); if (request.getInterviewDate() == null || request.getInterviewTime() == null) throw new IllegalArgumentException("Interview date and time are required"); String mode = request.getInterviewMode() == null ? "" : request.getInterviewMode().trim().toUpperCase(); if (mode.isBlank()) throw new IllegalArgumentException("Interview mode is required"); if (request.getInterviewDate().isBefore(java.time.LocalDate.now())) throw new IllegalArgumentException("Interview date must be today or later"); if ("ONLINE".equals(mode) && (request.getMeetingLink() == null || request.getMeetingLink().isBlank())) throw new IllegalArgumentException("Online interviews require a meeting link"); r.setInterviewDate(request.getInterviewDate()); r.setInterviewTime(request.getInterviewTime()); r.setInterviewMode(mode); r.setMeetingLink(request.getMeetingLink()); r.setInterviewAt(java.time.LocalDateTime.of(request.getInterviewDate(), request.getInterviewTime())); r.setInterviewNotes(request.getNotes()); r.setInterviewStatus("SCHEDULED"); r.setStatus("INTERVIEW_SCHEDULED"); return toResponse(repository.save(r)); }
    public RecruitmentResponse decision(Long id, StatusRequest request) { RecruitmentRecord r = require(id); String status = request.getStatus().trim().toUpperCase(); if (!List.of("HIRED", "REJECTED", "FINAL_REJECTED").contains(status)) throw new IllegalArgumentException("Decision status must be HIRED, REJECTED, or FINAL_REJECTED"); if ("HIRED".equals(status) && !"COMPLETED".equals(r.getInterviewStatus())) throw new IllegalArgumentException("Interview must be completed before hiring"); r.setStatus(status); r.setDecisionNotes(request.getNotes()); return toResponse(repository.save(r)); }
    private RecruitmentRecord require(Long id) { return repository.findById(id).orElseThrow(() -> new RecruitmentExceptions.NotFound(id)); }
    private RecruitmentResponse toResponse(RecruitmentRecord r) { return new RecruitmentResponse(r.getId(), r.getApplicationId(), r.getCandidateId(), r.getJobId(), r.getStatus(), r.getInterviewAt(), r.getInterviewDate(), r.getInterviewTime(), r.getInterviewMode(), r.getMeetingLink(), r.getInterviewStatus(), r.getInterviewNotes(), r.getDecisionNotes(), r.getCreatedAt(), r.getUpdatedAt()); }
}
