package com.talentsync.recruitmentservice.service;

import com.talentsync.recruitmentservice.dto.RecruitmentRequest;
import com.talentsync.recruitmentservice.dto.StatusRequest;
import com.talentsync.recruitmentservice.entity.RecruitmentRecord;
import com.talentsync.recruitmentservice.repository.RecruitmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecruitmentServiceTest {
    @Mock RecruitmentRepository repository; @InjectMocks RecruitmentService service;
    @Test void createsAndAdvancesRecruitment() {
        RecruitmentRequest request = new RecruitmentRequest(); request.setApplicationId(1L); request.setCandidateId(2L); request.setJobId(3L);
        when(repository.findByApplicationId(1L)).thenReturn(java.util.Optional.empty()); when(repository.save(any(RecruitmentRecord.class))).thenAnswer(i -> { RecruitmentRecord r = i.getArgument(0); r.setId(4L); return r; });
        assertEquals("IN_REVIEW", service.create(request).getStatus());
        RecruitmentRecord stored = new RecruitmentRecord(); stored.setId(4L); stored.setApplicationId(1L); stored.setCandidateId(2L); stored.setJobId(3L); stored.setStatus("IN_REVIEW"); when(repository.findById(4L)).thenReturn(java.util.Optional.of(stored));
        StatusRequest status = new StatusRequest(); status.setStatus("shortlisted"); assertEquals("SHORTLISTED", service.updateStatus(4L, status).getStatus());
    }
}
