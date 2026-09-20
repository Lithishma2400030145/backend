package com.talentsync.jobservice.service.impl;

import com.talentsync.jobservice.dto.JobRequestDTO;
import com.talentsync.jobservice.dto.JobResponseDTO;
import com.talentsync.jobservice.entity.Job;
import com.talentsync.jobservice.exception.InvalidJobDataException;
import com.talentsync.jobservice.exception.JobNotFoundException;
import com.talentsync.jobservice.repository.JobRepository;
import com.talentsync.jobservice.service.IJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Job Service Implementation
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class JobServiceImpl implements IJobService {

    private final JobRepository jobRepository;

    @Override
    public JobResponseDTO createJob(JobRequestDTO jobRequestDTO) {
        // Validate input
        validateJobRequestDTO(jobRequestDTO);
        log.info("Creating new job: {}", jobRequestDTO.getTitle());

        // Create entity from DTO
        Job job = new Job();
        mapDtoToEntity(jobRequestDTO, job);

        // Save to database
        Job savedJob = jobRepository.save(job);
        log.info("Job created successfully with id: {}", savedJob.getId());

        return mapEntityToDto(savedJob);
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponseDTO getJobById(Long id) {
        log.info("Fetching job with id: {}", id);

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Job not found with id: {}", id);
                    return JobNotFoundException.forId(id);
                });

        return mapEntityToDto(job);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobResponseDTO> getAllJobs(Pageable pageable) {
        log.info("Fetching all jobs with pagination: {}", pageable);

        return jobRepository.findAll(pageable)
                .map(this::mapEntityToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponseDTO> getJobsByStatus(String status) {
        log.info("Fetching jobs with status: {}", status);

        return jobRepository.findByStatus(status).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponseDTO> getJobsByCompany(String company) {
        log.info("Fetching jobs from company: {}", company);

        return jobRepository.findByCompany(company).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponseDTO> getJobsByLocation(String location) {
        log.info("Fetching jobs in location: {}", location);

        return jobRepository.findByLocation(location).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponseDTO> getJobsByExperienceLevel(String experienceLevel) {
        log.info("Fetching jobs for experience level: {}", experienceLevel);

        return jobRepository.findByExperienceLevel(experienceLevel).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobResponseDTO> searchJobs(String keyword, Pageable pageable) {
        log.info("Searching jobs with keyword: {}", keyword);

        return jobRepository.searchJobs(keyword, pageable)
                .map(this::mapEntityToDto);
    }

    @Override
    public JobResponseDTO updateJob(Long id, JobRequestDTO jobRequestDTO) {
        log.info("Updating job with id: {}", id);

        // Validate input
        validateJobRequestDTO(jobRequestDTO);

        // Fetch existing job
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Job not found with id: {}", id);
                    return JobNotFoundException.forId(id);
                });

        // Update fields
        mapDtoToEntity(jobRequestDTO, job);

        // Save updated job
        Job updatedJob = jobRepository.save(job);
        log.info("Job updated successfully with id: {}", id);

        return mapEntityToDto(updatedJob);
    }

    @Override
    public void deleteJob(Long id) {
        log.info("Deleting job with id: {}", id);

        if (!jobRepository.existsById(id)) {
            log.error("Job not found with id: {}", id);
            throw JobNotFoundException.forId(id);
        }

        jobRepository.deleteById(id);
        log.info("Job deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countJobsByStatus(String status) {
        log.info("Counting jobs with status: {}", status);
        return jobRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobResponseDTO> filterJobs(String status, String company, String location, 
                                           String experienceLevel, Pageable pageable) {
        log.info("Filtering jobs - status: {}, company: {}, location: {}, experienceLevel: {}", 
                status, company, location, experienceLevel);

        // For now, implement basic filtering by status
        // Can be extended to support multiple criteria
        if (status != null && !status.isEmpty()) {
            return jobRepository.findByStatus(status, pageable)
                    .map(this::mapEntityToDto);
        }

        return jobRepository.findAll(pageable)
                .map(this::mapEntityToDto);
    }

    /**
     * Validate JobRequestDTO
     */
    private void validateJobRequestDTO(JobRequestDTO dto) {
        if (dto == null) {
            throw new InvalidJobDataException("Job data cannot be null");
        }

        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new InvalidJobDataException("Job title is required");
        }

        if (dto.getCompany() == null || dto.getCompany().trim().isEmpty()) {
            throw new InvalidJobDataException("Company name is required");
        }

        if (dto.getLocation() == null || dto.getLocation().trim().isEmpty()) {
            throw new InvalidJobDataException("Location is required");
        }

        if (dto.getSalary() == null || dto.getSalary().signum() <= 0) {
            throw new InvalidJobDataException("Valid salary is required");
        }
    }

    /**
     * Map DTO to Entity
     */
    private void mapDtoToEntity(JobRequestDTO dto, Job entity) {
        entity.setTitle(dto.getTitle());
        entity.setCompany(dto.getCompany());
        entity.setLocation(dto.getLocation());
        entity.setDescription(dto.getDescription());
        entity.setRequiredSkills(dto.getRequiredSkills());
        entity.setSalary(dto.getSalary());
        entity.setSalaryType(dto.getSalaryType());
        entity.setStatus(dto.getStatus());
        entity.setJobType(dto.getJobType());
        entity.setCategory(dto.getCategory());
        entity.setBenefits(dto.getBenefits());
        entity.setExperienceLevel(dto.getExperienceLevel());
    }

    /**
     * Map Entity to DTO
     */
    private JobResponseDTO mapEntityToDto(Job entity) {
        return new JobResponseDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getCompany(),
                entity.getLocation(),
                entity.getDescription(),
                entity.getRequiredSkills(),
                entity.getSalary(),
                entity.getSalaryType(),
                entity.getStatus(),
                entity.getJobType(),
                entity.getCategory(),
                entity.getBenefits(),
                entity.getExperienceLevel(),
                entity.getApplicationCount(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCreatedBy(),
                entity.getUpdatedBy()
        );
    }
}
