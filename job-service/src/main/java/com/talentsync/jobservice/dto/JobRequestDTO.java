package com.talentsync.jobservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Job Request DTO
 * 
 * Used for creating and updating job postings.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequestDTO {

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Company name is required")
    private String company;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Job description is required")
    private String description;

    private String requiredSkills;

    @NotNull(message = "Salary is required")
    private BigDecimal salary;

    private String salaryType; // MONTHLY, YEARLY, etc.

    @NotBlank(message = "Job status is required")
    private String status; // OPEN, CLOSED, DRAFT, etc.

    private String jobType; // FULL_TIME, PART_TIME, CONTRACT, etc.

    private String category;

    private String benefits;

    private String experienceLevel; // ENTRY_LEVEL, JUNIOR, SENIOR, etc.
}
