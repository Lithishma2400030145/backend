package com.talentsync.authservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "uk_users_email", columnNames = "email"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 255) private String name;
    @Column(nullable = false, unique = true, length = 255) private String email;
    @Column(nullable = false) private String passwordHash;
    @Column(nullable = false, length = 30) private String role;
    @Column(nullable = false) private LocalDateTime createdAt;
    @PrePersist void created() { createdAt = LocalDateTime.now(); }
}
