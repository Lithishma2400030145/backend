package com.talentsync.applicationservice.service;

import com.talentsync.applicationservice.client.JobClient;
import com.talentsync.applicationservice.dto.ApplicationRequest;
import com.talentsync.applicationservice.entity.JobApplication;
import com.talentsync.applicationservice.exception.DuplicateApplicationException;
import com.talentsync.applicationservice.repository.JobApplicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {
    @Mock JobApplicationRepository repository;
    @Mock JobClient jobClient;
    @InjectMocks ApplicationService service;

    @Test void submitsOnlyWhenJobIsOpen() {
        ApplicationRequest request = new ApplicationRequest(); request.setCandidateId(2L); request.setJobId(3L); request.setCoverLetter("cover");
        when(repository.existsByCandidateIdAndJobId(2L, 3L)).thenReturn(false);
        when(jobClient.getJob(3L)).thenReturn(new org.springframework.http.ResponseEntity<>(new JobClient.JobSummary(3L, "Java", "Acme", "Remote", "desc", "Java", BigDecimal.TEN, "YEARLY", "OPEN", "FULL_TIME", "IT", null, "SENIOR", 0, null, null, null, null), org.springframework.http.HttpStatus.OK));
        when(repository.save(any(JobApplication.class))).thenAnswer(invocation -> { JobApplication value = invocation.getArgument(0); value.setId(10L); return value; });
        assertEquals(10L, service.submit(request).getId());
        verify(repository).save(any(JobApplication.class));
    }

    @Test void rejectsDuplicateApplication() {
        ApplicationRequest request = new ApplicationRequest(); request.setCandidateId(2L); request.setJobId(3L); request.setCoverLetter("cover");
        when(repository.existsByCandidateIdAndJobId(2L, 3L)).thenReturn(true);
        assertThrows(DuplicateApplicationException.class, () -> service.submit(request));
        verifyNoInteractions(jobClient);
    }
}
