package com.talentsync.authservice.service;

import com.talentsync.authservice.dto.*;
import com.talentsync.authservice.entity.User;
import com.talentsync.authservice.exception.AuthExceptions;
import com.talentsync.authservice.repository.UserRepository;
import com.talentsync.authservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Transactional
public class AuthService {
    private final UserRepository repository; private final PasswordEncoder encoder; private final JwtService jwtService;
    public AuthResponse register(RegisterRequest request) {
        String role = request.getRole().trim().toUpperCase();
        if (!role.equals("CANDIDATE") && !role.equals("RECRUITER")) throw new IllegalArgumentException("Role must be CANDIDATE or RECRUITER");
        if (repository.existsByEmailIgnoreCase(request.getEmail())) throw new AuthExceptions.Conflict("Email is already registered");
        User user = new User(); user.setName(request.getName().trim()); user.setEmail(request.getEmail().trim().toLowerCase()); user.setPasswordHash(encoder.encode(request.getPassword())); user.setRole(role);
        User saved = repository.save(user); return token(saved);
    }
    public AuthResponse login(LoginRequest request) {
        User user = repository.findByEmailIgnoreCase(request.getEmail()).orElseThrow(AuthExceptions.InvalidCredentials::new);
        if (!encoder.matches(request.getPassword(), user.getPasswordHash())) throw new AuthExceptions.InvalidCredentials();
        return token(user);
    }
    private AuthResponse token(User user) { UserDetails details = org.springframework.security.core.userdetails.User.withUsername(user.getEmail()).password(user.getPasswordHash()).roles(user.getRole()).build(); return new AuthResponse(jwtService.generate(details, user.getRole(), user.getId()), "Bearer", user.getId(), user.getName(), user.getEmail(), user.getRole()); }
}
