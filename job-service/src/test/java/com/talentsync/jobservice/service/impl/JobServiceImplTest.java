package com.talentsync.jobservice.service.impl;

import com.talentsync.jobservice.dto.JobRequestDTO;
import com.talentsync.jobservice.dto.JobResponseDTO;
import com.talentsync.jobservice.entity.Job;
import com.talentsync.jobservice.exception.InvalidJobDataException;
import com.talentsync.jobservice.exception.JobNotFoundException;
import com.talentsync.jobservice.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for JobService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("JobService Unit Tests")
class JobServiceImplTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobServiceImpl jobService;

    private Job job;
    private JobRequestDTO jobRequestDTO;
    private JobResponseDTO jobResponseDTO;

    @BeforeEach
    void setUp() {
        // Create sample job entity
        job = new Job();
        job.setId(1L);
        job.setTitle("Software Engineer");
        job.setCompany("TalentSync Inc");
        job.setLocation("San Francisco");
        job.setDescription("Looking for experienced software engineer");
        job.setRequiredSkills("Java, Spring Boot, MySQL");
        job.setSalary(new BigDecimal("120000"));
        job.setSalaryType("YEARLY");
        job.setStatus("OPEN");
        job.setJobType("FULL_TIME");
        job.setCategory("IT");
        job.setBenefits("Health Insurance, 401k");
        job.setExperienceLevel("SENIOR");
        job.setApplicationCount(5);
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());
        job.setCreatedBy("admin");
        job.setUpdatedBy("admin");

        // Create sample DTO
        jobRequestDTO = new JobRequestDTO();
        jobRequestDTO.setTitle("Software Engineer");
        jobRequestDTO.setCompany("TalentSync Inc");
        jobRequestDTO.setLocation("San Francisco");
        jobRequestDTO.setDescription("Looking for experienced software engineer");
        jobRequestDTO.setRequiredSkills("Java, Spring Boot, MySQL");
        jobRequestDTO.setSalary(new BigDecimal("120000"));
        jobRequestDTO.setSalaryType("YEARLY");
        jobRequestDTO.setStatus("OPEN");
        jobRequestDTO.setJobType("FULL_TIME");
        jobRequestDTO.setCategory("IT");
        jobRequestDTO.setBenefits("Health Insurance, 401k");
        jobRequestDTO.setExperienceLevel("SENIOR");

        // Create response DTO
        jobResponseDTO = new JobResponseDTO(
                1L, "Software Engineer", "TalentSync Inc", "San Francisco",
                "Looking for experienced software engineer", "Java, Spring Boot, MySQL",
                new BigDecimal("120000"), "YEARLY", "OPEN", "FULL_TIME", "IT",
                "Health Insurance, 401k", "SENIOR", 5,
                LocalDateTime.now(), LocalDateTime.now(), "admin", "admin"
        );
    }

    @Test
    @DisplayName("Should create a job successfully")
    void testCreateJob_Success() {
        // Arrange
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        // Act
        JobResponseDTO result = jobService.createJob(jobRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(job.getTitle(), result.getTitle());
        assertEquals(job.getCompany(), result.getCompany());
        assertEquals(job.getLocation(), result.getLocation());
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    @DisplayName("Should throw exception when creating job with null data")
    void testCreateJob_NullData() {
        // Act & Assert
        assertThrows(InvalidJobDataException.class, () -> jobService.createJob(null));
        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    @DisplayName("Should throw exception when creating job with empty title")
    void testCreateJob_EmptyTitle() {
        // Arrange
        jobRequestDTO.setTitle("");

        // Act & Assert
        assertThrows(InvalidJobDataException.class, () -> jobService.createJob(jobRequestDTO));
        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    @DisplayName("Should throw exception when creating job with negative salary")
    void testCreateJob_NegativeSalary() {
        // Arrange
        jobRequestDTO.setSalary(new BigDecimal("-1000"));

        // Act & Assert
        assertThrows(InvalidJobDataException.class, () -> jobService.createJob(jobRequestDTO));
        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    @DisplayName("Should retrieve job by id successfully")
    void testGetJobById_Success() {
        // Arrange
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));

        // Act
        JobResponseDTO result = jobService.getJobById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(job.getTitle(), result.getTitle());
        verify(jobRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when job not found by id")
    void testGetJobById_NotFound() {
        // Arrange
        when(jobRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(JobNotFoundException.class, () -> jobService.getJobById(999L));
        verify(jobRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should retrieve all jobs with pagination")
    void testGetAllJobs_Success() {
        // Arrange
        List<Job> jobList = new ArrayList<>();
        jobList.add(job);
        Page<Job> jobPage = new PageImpl<>(jobList);
        Pageable pageable = PageRequest.of(0, 10);

        when(jobRepository.findAll(pageable)).thenReturn(jobPage);

        // Act
        Page<JobResponseDTO> result = jobService.getAllJobs(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(jobRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Should retrieve jobs by status")
    void testGetJobsByStatus_Success() {
        // Arrange
        List<Job> jobList = new ArrayList<>();
        jobList.add(job);
        when(jobRepository.findByStatus("OPEN")).thenReturn(jobList);

        // Act
        List<JobResponseDTO> result = jobService.getJobsByStatus("OPEN");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("OPEN", result.get(0).getStatus());
        verify(jobRepository, times(1)).findByStatus("OPEN");
    }

    @Test
    @DisplayName("Should retrieve jobs by company")
    void testGetJobsByCompany_Success() {
        // Arrange
        List<Job> jobList = new ArrayList<>();
        jobList.add(job);
        when(jobRepository.findByCompany("TalentSync Inc")).thenReturn(jobList);

        // Act
        List<JobResponseDTO> result = jobService.getJobsByCompany("TalentSync Inc");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TalentSync Inc", result.get(0).getCompany());
        verify(jobRepository, times(1)).findByCompany("TalentSync Inc");
    }

    @Test
    @DisplayName("Should retrieve jobs by location")
    void testGetJobsByLocation_Success() {
        // Arrange
        List<Job> jobList = new ArrayList<>();
        jobList.add(job);
        when(jobRepository.findByLocation("San Francisco")).thenReturn(jobList);

        // Act
        List<JobResponseDTO> result = jobService.getJobsByLocation("San Francisco");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("San Francisco", result.get(0).getLocation());
        verify(jobRepository, times(1)).findByLocation("San Francisco");
    }

    @Test
    @DisplayName("Should update job successfully")
    void testUpdateJob_Success() {
        // Arrange
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        // Act
        JobResponseDTO result = jobService.updateJob(1L, jobRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(job.getTitle(), result.getTitle());
        verify(jobRepository, times(1)).findById(1L);
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent job")
    void testUpdateJob_NotFound() {
        // Arrange
        when(jobRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(JobNotFoundException.class, () -> jobService.updateJob(999L, jobRequestDTO));
        verify(jobRepository, times(1)).findById(999L);
        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    @DisplayName("Should delete job successfully")
    void testDeleteJob_Success() {
        // Arrange
        when(jobRepository.existsById(1L)).thenReturn(true);
        doNothing().when(jobRepository).deleteById(1L);

        // Act
        jobService.deleteJob(1L);

        // Assert
        verify(jobRepository, times(1)).existsById(1L);
        verify(jobRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent job")
    void testDeleteJob_NotFound() {
        // Arrange
        when(jobRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(JobNotFoundException.class, () -> jobService.deleteJob(999L));
        verify(jobRepository, times(1)).existsById(999L);
        verify(jobRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should count jobs by status")
    void testCountJobsByStatus_Success() {
        // Arrange
        when(jobRepository.countByStatus("OPEN")).thenReturn(5L);

        // Act
        Long result = jobService.countJobsByStatus("OPEN");

        // Assert
        assertEquals(5L, result);
        verify(jobRepository, times(1)).countByStatus("OPEN");
    }

    @Test
    @DisplayName("Should search jobs by keyword")
    void testSearchJobs_Success() {
        // Arrange
        List<Job> jobList = new ArrayList<>();
        jobList.add(job);
        Page<Job> jobPage = new PageImpl<>(jobList);
        Pageable pageable = PageRequest.of(0, 10);

        when(jobRepository.searchJobs("Software", pageable)).thenReturn(jobPage);

        // Act
        Page<JobResponseDTO> result = jobService.searchJobs("Software", pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(jobRepository, times(1)).searchJobs("Software", pageable);
    }
}
