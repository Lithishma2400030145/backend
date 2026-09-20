package com.talentsync.applicationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talentsync.applicationservice.dto.ApplicationRequest;
import com.talentsync.applicationservice.dto.ApplicationResponse;
import com.talentsync.applicationservice.service.ApplicationService;
import com.talentsync.applicationservice.service.ResumeStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApplicationController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = "security.jwt.secret=test-secret-with-at-least-32-characters-long")
class ApplicationControllerWebTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean ApplicationService service;
    @MockBean ResumeStorageService resumeStorage;

    @Test void acceptsValidSubmission() throws Exception {
        when(service.submit(any())).thenReturn(new ApplicationResponse(1L, 2L, 3L, "SUBMITTED", "cover", null, LocalDateTime.now(), LocalDateTime.now()));
        String json = "{\"candidateId\":2,\"jobId\":3,\"coverLetter\":\"cover\"}";
        mvc.perform(post("/api/applications").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated());
    }

    @Test void rejectsInvalidSubmission() throws Exception {
        mvc.perform(post("/api/applications").contentType(MediaType.APPLICATION_JSON).content("{\"candidateId\":2}"))
                .andExpect(status().isBadRequest());
    }

    @Test void acceptsMultipartSubmissionWithResume() throws Exception {
        when(service.submit(any(ApplicationRequest.class), any())).thenReturn(new ApplicationResponse(2L, 2L, 3L, "SUBMITTED", "cover", "resume.pdf", LocalDateTime.now(), LocalDateTime.now()));
        mvc.perform(multipart("/api/applications")
                        .file("resume", "resume".getBytes())
                        .param("candidateId", "2")
                        .param("jobId", "3")
                        .param("coverLetter", "cover")
                )
                .andExpect(status().isCreated());
    }
}
