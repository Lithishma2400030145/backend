package com.talentsync.recruitmentservice.controller;

import com.talentsync.recruitmentservice.dto.*;
import com.talentsync.recruitmentservice.service.RecruitmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/recruitment")
@RequiredArgsConstructor
public class RecruitmentController {
    private final RecruitmentService service;
    @PostMapping @PreAuthorize("hasRole('RECRUITER')") public ResponseEntity<RecruitmentResponse> create(@Valid @RequestBody RecruitmentRequest request) { RecruitmentResponse r = service.create(request); return ResponseEntity.created(URI.create("/api/recruitment/" + r.getId())).body(r); }
    @GetMapping("/{id}") public RecruitmentResponse get(@PathVariable Long id) { return service.byApplication(id); }
    @GetMapping("/candidate/{id}") public List<RecruitmentResponse> byCandidate(@PathVariable Long id) { return service.byCandidate(id); }
    @GetMapping("/job/{id}") public List<RecruitmentResponse> byJob(@PathVariable Long id) { return service.byJob(id); }
    @GetMapping("/status/{status}") public List<RecruitmentResponse> byStatus(@PathVariable String status) { return service.byStatus(status); }
    @PatchMapping("/{id}/status") @PreAuthorize("hasRole('RECRUITER')") public RecruitmentResponse status(@PathVariable Long id, @Valid @RequestBody StatusRequest request) { return service.updateStatus(id, request); }
    @PatchMapping("/{id}/interview") @PreAuthorize("hasRole('RECRUITER')") public RecruitmentResponse interview(@PathVariable Long id, @Valid @RequestBody InterviewRequest request) { return service.schedule(id, request); }
    @PatchMapping("/{id}/decision") @PreAuthorize("hasRole('RECRUITER')") public RecruitmentResponse decision(@PathVariable Long id, @Valid @RequestBody StatusRequest request) { return service.decision(id, request); }
}
