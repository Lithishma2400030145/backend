package com.talentsync.applicationservice.controller;

import com.talentsync.applicationservice.dto.ApplicationRequest;
import com.talentsync.applicationservice.dto.ApplicationResponse;
import com.talentsync.applicationservice.dto.ApplicationStatusRequest;
import com.talentsync.applicationservice.dto.MultipartApplicationRequest;
import com.talentsync.applicationservice.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService service;
    private final com.talentsync.applicationservice.service.ResumeStorageService resumeStorage;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApplicationResponse> submit(@Valid @RequestBody ApplicationRequest request) {
        ApplicationResponse response = service.submit(request);
        return ResponseEntity.created(URI.create("/api/applications/" + response.getId())).body(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApplicationResponse> submitWithResume(@Valid @ModelAttribute MultipartApplicationRequest multipartRequest) {
        ApplicationRequest request = new ApplicationRequest();
        request.setCandidateId(multipartRequest.getCandidateId());
        request.setJobId(multipartRequest.getJobId());
        request.setCoverLetter(multipartRequest.getCoverLetter());
        ApplicationResponse response = service.submit(request, multipartRequest.getResume());
        return ResponseEntity.created(URI.create("/api/applications/" + response.getId())).body(response);
    }

    @GetMapping("/{id}")
    public ApplicationResponse get(@PathVariable Long id) { return service.get(id); }

    @GetMapping("/{id}/resume")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'RECRUITER')")
    public ResponseEntity<Resource> resume(@PathVariable Long id) {
        String reference = service.resumeReference(id);
        Resource resource = resumeStorage.load(reference);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resumeStorage.contentType(reference)))
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline().filename("resume-" + id).build().toString())
                .body(resource);
    }

    @GetMapping("/candidate/{candidateId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public List<ApplicationResponse> byCandidate(@PathVariable Long candidateId) { return service.byCandidate(candidateId); }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public List<ApplicationResponse> byJob(@PathVariable Long jobId) { return service.byJob(jobId); }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ApplicationResponse updateStatus(@PathVariable Long id, @Valid @RequestBody ApplicationStatusRequest request) { return service.updateStatus(id, request); }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
