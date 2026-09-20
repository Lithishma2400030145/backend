package com.talentsync.authservice.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationMs;
    public JwtService(@Value("${security.jwt.secret}") String secret, @Value("${security.jwt.expiration-ms:3600000}") long expirationMs) {
        if (secret.length() < 32) throw new IllegalArgumentException("security.jwt.secret must contain at least 32 characters");
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); this.expirationMs = expirationMs;
    }
    public String generate(UserDetails user, String role, Long userId) {
        Instant now = Instant.now();
        return Jwts.builder().subject(user.getUsername()).claim("role", role).claim("userId", userId)
                .issuedAt(Date.from(now)).expiration(new Date(now.toEpochMilli() + expirationMs)).signWith(key).compact();
    }
    public String username(String token) { return claims(token).getSubject(); }
    public boolean valid(String token, UserDetails user) { try { return username(token).equalsIgnoreCase(user.getUsername()) && !claims(token).getExpiration().before(new Date()); } catch (JwtException | IllegalArgumentException ex) { return false; } }
    private Claims claims(String token) { return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload(); }
}
