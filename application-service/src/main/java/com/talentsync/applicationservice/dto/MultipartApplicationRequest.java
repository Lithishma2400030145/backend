package com.talentsync.applicationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MultipartApplicationRequest {
    @NotNull
    private Long candidateId;
    @NotNull
    private Long jobId;
    @NotBlank
    private String coverLetter;
    @NotNull
    private MultipartFile resume;
}
