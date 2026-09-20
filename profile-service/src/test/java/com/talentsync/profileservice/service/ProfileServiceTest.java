package com.talentsync.profileservice.service;

import com.talentsync.profileservice.dto.ProfileRequest;
import com.talentsync.profileservice.entity.Profile;
import com.talentsync.profileservice.repository.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {
    @Mock ProfileRepository repository; @InjectMocks ProfileService service;
    @Test void createsCandidateProfile() {
        ProfileRequest request = new ProfileRequest(); request.setUserId(9L); request.setRole("candidate"); request.setFullName("Candidate"); request.setSkills("Java");
        when(repository.existsByUserId(9L)).thenReturn(false); when(repository.save(any(Profile.class))).thenAnswer(i -> { Profile p = i.getArgument(0); p.setId(10L); return p; });
        assertEquals("CANDIDATE", service.create(request).getRole()); verify(repository).save(any(Profile.class));
    }
}
