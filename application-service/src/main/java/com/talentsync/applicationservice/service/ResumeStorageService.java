package com.talentsync.applicationservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class ResumeStorageService {
    public static final long MAX_SIZE = 5 * 1024 * 1024;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "doc", "docx");
    private final Path root;

    public ResumeStorageService(@Value("${app.upload-dir:uploads/resumes}") String uploadDirectory) {
        root = Paths.get(uploadDirectory).toAbsolutePath().normalize();
    }

    @PostConstruct
    void initialize() {
        try {
            Files.createDirectories(root);
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to create resume upload directory", ex);
        }
    }

    public String store(MultipartFile file) {
        validate(file);
        String extension = extension(file.getOriginalFilename());
        String storedName = UUID.randomUUID() + "." + extension;
        try {
            file.transferTo(root.resolve(storedName));
            return storedName;
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to store resume", ex);
        }
    }

    public Resource load(String storedName) {
        if (storedName == null || storedName.isBlank() || storedName.contains("/") || storedName.contains("\\") || storedName.contains("..")) {
            throw new IllegalArgumentException("Resume reference is invalid");
        }
        Path file = root.resolve(storedName).normalize();
        if (!file.startsWith(root) || !Files.isRegularFile(file)) {
            throw new IllegalArgumentException("Resume file was not found");
        }
        return new FileSystemResource(file);
    }

    public String contentType(String storedName) {
        String extension = extension(storedName);
        return switch (extension) {
            case "pdf" -> "application/pdf";
            case "doc" -> "application/msword";
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            default -> "application/octet-stream";
        };
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Resume file is required");
        if (file.getSize() > MAX_SIZE) throw new IllegalArgumentException("Resume file must be 5 MB or smaller");
        if (!ALLOWED_EXTENSIONS.contains(extension(file.getOriginalFilename()))) {
            throw new IllegalArgumentException("Resume must be a PDF, DOC, or DOCX file");
        }
    }

    private String extension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }
}
