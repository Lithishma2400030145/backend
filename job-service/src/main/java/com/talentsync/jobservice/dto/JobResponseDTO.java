package com.talentsync.jobservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Job Response DTO
 * 
 * Used for returning job information to clients.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobResponseDTO {

    private Long id;

    private String title;

    private String company;

    private String location;

    private String description;

    private String requiredSkills;

    private BigDecimal salary;

    private String salaryType;

    private String status;

    private String jobType;

    private String category;

    private String benefits;

    private String experienceLevel;

    private Integer applicationCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String createdBy;

    private String updatedBy;
}
