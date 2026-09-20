package com.talentsync.recruitmentservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StatusRequest {
    @NotBlank private String status;
    private String notes;
}
