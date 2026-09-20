package com.talentsync.profileservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data @AllArgsConstructor
public class ProfileResponse {
    private Long id; private Long userId; private String role; private String fullName; private String email; private String phone; private String location; private String bio; private String skills; private String company; private String resumeUrl; private LocalDateTime createdAt; private LocalDateTime updatedAt;
}
