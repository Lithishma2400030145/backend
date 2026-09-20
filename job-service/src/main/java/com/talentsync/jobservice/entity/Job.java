package com.talentsync.jobservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Job Entity
 * 
 * Represents a job posting in the TalentSync system.
 */
@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Job title is required")
    @Column(nullable = false, length = 255)
    private String title;

    @NotBlank(message = "Company name is required")
    @Column(nullable = false, length = 255)
    private String company;

    @NotBlank(message = "Location is required")
    @Column(nullable = false, length = 255)
    private String location;

    @NotBlank(message = "Job description is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requiredSkills;

    @NotNull(message = "Salary is required")
    @Column(nullable = false)
    private BigDecimal salary;

    @Column(length = 50)
    private String salaryType; // MONTHLY, YEARLY, etc.

    @NotBlank(message = "Job status is required")
    @Column(nullable = false, length = 50)
    private String status; // OPEN, CLOSED, DRAFT, etc.

    @Column(length = 500)
    private String jobType; // FULL_TIME, PART_TIME, CONTRACT, etc.

    @Column(length = 500)
    private String category; // Job category

    @Column(columnDefinition = "TEXT")
    private String benefits;

    @Column(name = "experience_level", length = 100)
    private String experienceLevel; // ENTRY_LEVEL, JUNIOR, SENIOR, etc.

    @Column(name = "application_count", columnDefinition = "INT DEFAULT 0")
    private Integer applicationCount = 0;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 255)
    private String createdBy;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;
}
