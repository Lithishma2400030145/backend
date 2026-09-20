package com.talentsync.applicationservice.client;

import com.talentsync.applicationservice.config.FeignAuthConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "job-service", configuration = FeignAuthConfig.class)
public interface JobClient {
    @GetMapping("/api/jobs/{id}")
    ResponseEntity<JobSummary> getJob(@PathVariable("id") Long id);

    record JobSummary(Long id, String title, String company, String location, String description,
                      String requiredSkills, java.math.BigDecimal salary, String salaryType,
                      String status, String jobType, String category, String benefits,
                      String experienceLevel, Integer applicationCount,
                      java.time.LocalDateTime createdAt, java.time.LocalDateTime updatedAt,
                      String createdBy, String updatedBy) {}
}
