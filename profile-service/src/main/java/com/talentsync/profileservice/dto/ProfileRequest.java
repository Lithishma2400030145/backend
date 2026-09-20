package com.talentsync.profileservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProfileRequest {
    @NotNull private Long userId;
    @NotBlank private String role;
    @NotBlank private String fullName;
    private String email; private String phone; private String location; private String bio; private String skills; private String company; private String resumeUrl;
}
