package com.talentsync.recruitmentservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RecruitmentRequest {
    @NotNull private Long applicationId;
    @NotNull private Long candidateId;
    @NotNull private Long jobId;
}
