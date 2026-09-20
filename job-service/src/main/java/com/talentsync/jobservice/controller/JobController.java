package com.talentsync.jobservice.controller;

import com.talentsync.jobservice.dto.JobRequestDTO;
import com.talentsync.jobservice.dto.JobResponseDTO;
import com.talentsync.jobservice.service.IJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Job Controller
 * 
 * REST API endpoints for Job Management.
 * Base path: /api/jobs
 */
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Job Management", description = "APIs for job posting management")
public class JobController {

    private final IJobService jobService;

    /**
     * POST /api/jobs
     * Create a new job
     */
    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Create a new job posting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Job created successfully",
                    content = @Content(schema = @Schema(implementation = JobResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid job data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<JobResponseDTO> createJob(@Valid @RequestBody JobRequestDTO jobRequestDTO) {
        log.info("POST /api/jobs - Creating new job");
        JobResponseDTO createdJob = jobService.createJob(jobRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
    }

    /**
     * GET /api/jobs
     * Get all jobs with pagination
     */
    @GetMapping
    @Operation(summary = "Get all jobs with pagination")
    @ApiResponse(responseCode = "200", description = "Jobs retrieved successfully")
    public ResponseEntity<Map<String, Object>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortOrder) {

        log.info("GET /api/jobs - Fetching all jobs (page: {}, size: {})", page, size);

        Sort.Direction direction = Sort.Direction.fromString(sortOrder.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<JobResponseDTO> jobs = jobService.getAllJobs(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("jobs", jobs.getContent());
        response.put("currentPage", jobs.getNumber());
        response.put("totalItems", jobs.getTotalElements());
        response.put("totalPages", jobs.getTotalPages());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/jobs/{id}
     * Get a specific job by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a job by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job found",
                    content = @Content(schema = @Schema(implementation = JobResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Job not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<JobResponseDTO> getJobById(@PathVariable Long id) {
        log.info("GET /api/jobs/{} - Fetching job by id", id);
        JobResponseDTO job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    /**
     * PUT /api/jobs/{id}
     * Update an existing job
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Update a job posting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job updated successfully",
                    content = @Content(schema = @Schema(implementation = JobResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid job data"),
            @ApiResponse(responseCode = "404", description = "Job not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<JobResponseDTO> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobRequestDTO jobRequestDTO) {

        log.info("PUT /api/jobs/{} - Updating job", id);
        JobResponseDTO updatedJob = jobService.updateJob(id, jobRequestDTO);
        return ResponseEntity.ok(updatedJob);
    }

    /**
     * DELETE /api/jobs/{id}
     * Delete a job
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(summary = "Delete a job posting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Job deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Job not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        log.info("DELETE /api/jobs/{} - Deleting job", id);
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/jobs/search
     * Search jobs by keyword
     */
    @GetMapping("/search")
    @Operation(summary = "Search jobs by keyword")
    @ApiResponse(responseCode = "200", description = "Search results retrieved")
    public ResponseEntity<Map<String, Object>> searchJobs(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("GET /api/jobs/search - Searching jobs with keyword: {}", keyword);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<JobResponseDTO> jobs = jobService.searchJobs(keyword, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("jobs", jobs.getContent());
        response.put("currentPage", jobs.getNumber());
        response.put("totalItems", jobs.getTotalElements());
        response.put("totalPages", jobs.getTotalPages());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/jobs/filter
     * Filter jobs by multiple criteria
     */
    @GetMapping("/filter")
    @Operation(summary = "Filter jobs by criteria")
    @ApiResponse(responseCode = "200", description = "Filtered jobs retrieved")
    public ResponseEntity<Map<String, Object>> filterJobs(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String experienceLevel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("GET /api/jobs/filter - Filtering jobs");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<JobResponseDTO> jobs = jobService.filterJobs(status, company, location, experienceLevel, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("jobs", jobs.getContent());
        response.put("currentPage", jobs.getNumber());
        response.put("totalItems", jobs.getTotalElements());
        response.put("totalPages", jobs.getTotalPages());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/jobs/by-status/{status}
     * Get all jobs by status
     */
    @GetMapping("/by-status/{status}")
    @Operation(summary = "Get jobs by status")
    @ApiResponse(responseCode = "200", description = "Jobs retrieved")
    public ResponseEntity<List<JobResponseDTO>> getJobsByStatus(@PathVariable String status) {
        log.info("GET /api/jobs/by-status/{} - Fetching jobs by status", status);
        List<JobResponseDTO> jobs = jobService.getJobsByStatus(status);
        return ResponseEntity.ok(jobs);
    }

    /**
     * GET /api/jobs/by-company/{company}
     * Get all jobs by company
     */
    @GetMapping("/by-company/{company}")
    @Operation(summary = "Get jobs by company")
    @ApiResponse(responseCode = "200", description = "Jobs retrieved")
    public ResponseEntity<List<JobResponseDTO>> getJobsByCompany(@PathVariable String company) {
        log.info("GET /api/jobs/by-company/{} - Fetching jobs by company", company);
        List<JobResponseDTO> jobs = jobService.getJobsByCompany(company);
        return ResponseEntity.ok(jobs);
    }

    /**
     * GET /api/jobs/by-location/{location}
     * Get all jobs by location
     */
    @GetMapping("/by-location/{location}")
    @Operation(summary = "Get jobs by location")
    @ApiResponse(responseCode = "200", description = "Jobs retrieved")
    public ResponseEntity<List<JobResponseDTO>> getJobsByLocation(@PathVariable String location) {
        log.info("GET /api/jobs/by-location/{} - Fetching jobs by location", location);
        List<JobResponseDTO> jobs = jobService.getJobsByLocation(location);
        return ResponseEntity.ok(jobs);
    }

    /**
     * GET /api/jobs/by-experience/{experienceLevel}
     * Get all jobs by experience level
     */
    @GetMapping("/by-experience/{experienceLevel}")
    @Operation(summary = "Get jobs by experience level")
    @ApiResponse(responseCode = "200", description = "Jobs retrieved")
    public ResponseEntity<List<JobResponseDTO>> getJobsByExperienceLevel(@PathVariable String experienceLevel) {
        log.info("GET /api/jobs/by-experience/{} - Fetching jobs by experience level", experienceLevel);
        List<JobResponseDTO> jobs = jobService.getJobsByExperienceLevel(experienceLevel);
        return ResponseEntity.ok(jobs);
    }

    /**
     * GET /api/jobs/count/by-status/{status}
     * Count jobs by status
     */
    @GetMapping("/count/by-status/{status}")
    @Operation(summary = "Count jobs by status")
    @ApiResponse(responseCode = "200", description = "Job count retrieved")
    public ResponseEntity<Map<String, Object>> countJobsByStatus(@PathVariable String status) {
        log.info("GET /api/jobs/count/by-status/{} - Counting jobs by status", status);
        Long count = jobService.countJobsByStatus(status);
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
}
