package com.talentsync.jobservice.service;

import com.talentsync.jobservice.dto.JobRequestDTO;
import com.talentsync.jobservice.dto.JobResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Job Service Interface
 */
public interface IJobService {

    /**
     * Create a new job
     */
    JobResponseDTO createJob(JobRequestDTO jobRequestDTO);

    /**
     * Get a job by id
     */
    JobResponseDTO getJobById(Long id);

    /**
     * Get all jobs with pagination
     */
    Page<JobResponseDTO> getAllJobs(Pageable pageable);

    /**
     * Get all jobs by status
     */
    List<JobResponseDTO> getJobsByStatus(String status);

    /**
     * Get all jobs by company
     */
    List<JobResponseDTO> getJobsByCompany(String company);

    /**
     * Get all jobs by location
     */
    List<JobResponseDTO> getJobsByLocation(String location);

    /**
     * Get all jobs by experience level
     */
    List<JobResponseDTO> getJobsByExperienceLevel(String experienceLevel);

    /**
     * Search jobs by keyword
     */
    Page<JobResponseDTO> searchJobs(String keyword, Pageable pageable);

    /**
     * Update a job
     */
    JobResponseDTO updateJob(Long id, JobRequestDTO jobRequestDTO);

    /**
     * Delete a job
     */
    void deleteJob(Long id);

    /**
     * Count jobs by status
     */
    Long countJobsByStatus(String status);

    /**
     * Filter jobs with multiple criteria
     */
    Page<JobResponseDTO> filterJobs(String status, String company, String location, String experienceLevel, Pageable pageable);
}
