package com.talentsync.applicationservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResumeStorageServiceTest {
    @TempDir
    Path uploadDirectory;

    @Test
    void storesAllowedResumeAndReturnsReference() throws Exception {
        ResumeStorageService storage = new ResumeStorageService(uploadDirectory.toString());
        storage.initialize();
        MockMultipartFile resume = new MockMultipartFile("resume", "candidate.pdf", "application/pdf", "resume".getBytes());

        String reference = storage.store(resume);

        assertEquals("pdf", reference.substring(reference.lastIndexOf('.') + 1));
        assertEquals("resume", Files.readString(uploadDirectory.resolve(reference)));
    }

    @Test
    void rejectsUnsupportedResumeType() {
        ResumeStorageService storage = new ResumeStorageService(uploadDirectory.toString());
        storage.initialize();
        MockMultipartFile resume = new MockMultipartFile("resume", "candidate.txt", "text/plain", "resume".getBytes());

        assertThrows(IllegalArgumentException.class, () -> storage.store(resume));
    }
}
