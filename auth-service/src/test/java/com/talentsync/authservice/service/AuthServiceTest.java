package com.talentsync.authservice.service;

import com.talentsync.authservice.dto.LoginRequest;
import com.talentsync.authservice.dto.RegisterRequest;
import com.talentsync.authservice.entity.User;
import com.talentsync.authservice.exception.AuthExceptions;
import com.talentsync.authservice.repository.UserRepository;
import com.talentsync.authservice.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock UserRepository repository; @Mock PasswordEncoder encoder; @Mock JwtService jwtService; @InjectMocks AuthService service;
    @Test void registersHashedPasswordAndReturnsToken() {
        RegisterRequest request = new RegisterRequest(); request.setName("Candidate"); request.setEmail("c@example.com"); request.setPassword("secret"); request.setRole("candidate");
        when(repository.existsByEmailIgnoreCase("c@example.com")).thenReturn(false); when(encoder.encode("secret")).thenReturn("hashed"); when(repository.save(any(User.class))).thenAnswer(i -> { User u = i.getArgument(0); u.setId(1L); return u; }); when(jwtService.generate(any(), eq("CANDIDATE"), eq(1L))).thenReturn("token");
        assertEquals("token", service.register(request).getToken()); verify(encoder).encode("secret");
    }
    @Test void rejectsBadPassword() {
        LoginRequest request = new LoginRequest(); request.setEmail("c@example.com"); request.setPassword("bad"); User user = new User(); user.setPasswordHash("hash"); when(repository.findByEmailIgnoreCase("c@example.com")).thenReturn(java.util.Optional.of(user)); when(encoder.matches("bad", "hash")).thenReturn(false);
        assertThrows(AuthExceptions.InvalidCredentials.class, () -> service.login(request));
    }

    @Test void rejectsDuplicateEmail() {
        RegisterRequest request = new RegisterRequest(); request.setName("Candidate"); request.setEmail("c@example.com"); request.setPassword("secret"); request.setRole("CANDIDATE");
        when(repository.existsByEmailIgnoreCase("c@example.com")).thenReturn(true);
        assertEquals("Email is already registered", assertThrows(AuthExceptions.Conflict.class, () -> service.register(request)).getMessage());
        verifyNoInteractions(encoder);
    }
}
