package com.talentsync.profileservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name = "profiles", uniqueConstraints = @UniqueConstraint(name = "uk_profile_user", columnNames = "user_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Profile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "user_id", nullable = false, unique = true) private Long userId;
    @Column(nullable = false, length = 30) private String role;
    @Column(nullable = false, length = 255) private String fullName;
    @Column(length = 255) private String email;
    @Column(length = 255) private String phone;
    @Column(length = 255) private String location;
    @Column(columnDefinition = "TEXT") private String bio;
    @Column(columnDefinition = "TEXT") private String skills;
    @Column(length = 500) private String company;
    @Column(length = 500) private String resumeUrl;
    @Column(nullable = false) private LocalDateTime createdAt;
    @Column(nullable = false) private LocalDateTime updatedAt;
    @PrePersist void create() { LocalDateTime now = LocalDateTime.now(); createdAt = now; updatedAt = now; }
    @PreUpdate void update() { updatedAt = LocalDateTime.now(); }
}
