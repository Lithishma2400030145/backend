package com.talentsync.recruitmentservice.repository;

import com.talentsync.recruitmentservice.entity.RecruitmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecruitmentRepository extends JpaRepository<RecruitmentRecord, Long> {
    Optional<RecruitmentRecord> findByApplicationId(Long applicationId);
    List<RecruitmentRecord> findByCandidateId(Long candidateId);
    List<RecruitmentRecord> findByJobId(Long jobId);
    List<RecruitmentRecord> findByStatus(String status);
}
