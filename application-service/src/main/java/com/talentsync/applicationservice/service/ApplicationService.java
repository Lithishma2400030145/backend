package com.talentsync.applicationservice.service;

import com.talentsync.applicationservice.client.JobClient;
import com.talentsync.applicationservice.dto.ApplicationRequest;
import com.talentsync.applicationservice.dto.ApplicationResponse;
import com.talentsync.applicationservice.dto.ApplicationStatusRequest;
import com.talentsync.applicationservice.entity.JobApplication;
import com.talentsync.applicationservice.exception.ApplicationNotFoundException;
import com.talentsync.applicationservice.exception.DuplicateApplicationException;
import com.talentsync.applicationservice.exception.JobUnavailableException;
import com.talentsync.applicationservice.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {
    private static final String DEFAULT_STATUS = "SUBMITTED";
    private final JobApplicationRepository repository;
    private final JobClient jobClient;
    private final ResumeStorageService resumeStorage;

    public ApplicationResponse submit(ApplicationRequest request) {
        if (repository.existsByCandidateIdAndJobId(request.getCandidateId(), request.getJobId())) {
            throw new DuplicateApplicationException(request.getCandidateId(), request.getJobId());
        }
        try {
            var response = jobClient.getJob(request.getJobId());
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null
                    || !"OPEN".equalsIgnoreCase(response.getBody().status())) {
                throw new JobUnavailableException(request.getJobId());
            }
        } catch (RuntimeException ex) {
            if (ex instanceof JobUnavailableException) throw ex;
            throw new JobUnavailableException(request.getJobId());
        }
        JobApplication application = new JobApplication();
        application.setCandidateId(request.getCandidateId());
        application.setJobId(request.getJobId());
        application.setCoverLetter(request.getCoverLetter());
        application.setResumeUrl(request.getResumeUrl());
        application.setStatus(DEFAULT_STATUS);
        return toResponse(repository.save(application));
    }

    public ApplicationResponse submit(ApplicationRequest request, MultipartFile resume) {
        ApplicationResponse response = submit(request);
        String storedName = resumeStorage.store(resume);
        JobApplication application = repository.findById(response.getId()).orElseThrow(() -> new ApplicationNotFoundException(response.getId()));
        application.setResumeUrl(storedName);
        return toResponse(repository.save(application));
    }

    @Transactional(readOnly = true)
    public ApplicationResponse get(Long id) {
        return repository.findById(id).map(this::toResponse).orElseThrow(() -> new ApplicationNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> byCandidate(Long candidateId) {
        return repository.findByCandidateId(candidateId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> byJob(Long jobId) {
        return repository.findByJobId(jobId).stream().map(this::toResponse).toList();
    }

    public ApplicationResponse updateStatus(Long id, ApplicationStatusRequest request) {
        JobApplication application = repository.findById(id).orElseThrow(() -> new ApplicationNotFoundException(id));
        application.setStatus(request.getStatus().trim().toUpperCase());
        return toResponse(repository.save(application));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new ApplicationNotFoundException(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public String resumeReference(Long id) {
        return repository.findById(id).map(JobApplication::getResumeUrl).orElseThrow(() -> new ApplicationNotFoundException(id));
    }

    private ApplicationResponse toResponse(JobApplication a) {
        return new ApplicationResponse(a.getId(), a.getCandidateId(), a.getJobId(), a.getStatus(), a.getCoverLetter(), a.getResumeUrl(), a.getAppliedAt(), a.getUpdatedAt());
    }
}
