package com.talentsync.applicationservice.repository;

import com.talentsync.applicationservice.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);
    List<JobApplication> findByCandidateId(Long candidateId);
    List<JobApplication> findByJobId(Long jobId);
}
