package com.talentsync.recruitmentservice.dto;

import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class InterviewRequest {
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate interviewDate;
    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime interviewTime;
    @NotNull
    private String interviewMode;
    private String meetingLink;
    private LocalDateTime interviewAt;
    private String notes;
}
