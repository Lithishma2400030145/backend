package com.talentsync.jobservice.repository;

import com.talentsync.jobservice.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Job Repository
 * 
 * Data access layer for Job entity using Spring Data JPA.
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    /**
     * Find all jobs by status
     */
    List<Job> findByStatus(String status);

    /**
     * Find all jobs by company
     */
    List<Job> findByCompany(String company);

    /**
     * Find all jobs by location
     */
    List<Job> findByLocation(String location);

    /**
     * Find jobs by title (case-insensitive)
     */
    List<Job> findByTitleIgnoreCase(String title);

    /**
     * Find jobs by experience level
     */
    List<Job> findByExperienceLevel(String experienceLevel);

    /**
     * Search jobs by title, company, or location with pagination
     */
    @Query("SELECT j FROM Job j WHERE " +
           "LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(j.company) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(j.location) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Job> searchJobs(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Find jobs by status and experience level
     */
    List<Job> findByStatusAndExperienceLevel(String status, String experienceLevel);

    /**
     * Find jobs by status and company
     */
    List<Job> findByStatusAndCompany(String status, String company);

    /**
     * Find jobs by status and location
     */
    Page<Job> findByStatus(String status, Pageable pageable);

    /**
     * Count jobs by status
     */
    Long countByStatus(String status);
}
