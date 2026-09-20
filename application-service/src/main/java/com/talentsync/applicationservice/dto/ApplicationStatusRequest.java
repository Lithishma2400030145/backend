package com.talentsync.applicationservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApplicationStatusRequest {
    @NotBlank
    private String status;
}
