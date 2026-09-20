package com.talentsync.recruitmentservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class JwtSecurityConfig { private final JwtFilter filter; public JwtSecurityConfig(JwtFilter filter) { this.filter = filter; } @Bean SecurityFilterChain security(HttpSecurity http) throws Exception { return http.csrf(csrf -> csrf.disable()).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(a -> a.requestMatchers("/actuator/health", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll().anyRequest().authenticated()).addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class).build(); } }
@Component
class JwtFilter extends OncePerRequestFilter { private final SecretKey key; JwtFilter(@Value("${security.jwt.secret}") String secret) { if (secret.length() < 32) throw new IllegalArgumentException("security.jwt.secret must contain at least 32 characters"); key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); } protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException { String h = request.getHeader("Authorization"); if (h != null && h.startsWith("Bearer ")) { try { var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(h.substring(7)).getPayload(); String role = claims.get("role", String.class); var auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, role == null ? List.of() : List.of(new SimpleGrantedAuthority("ROLE_" + role))); org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth); } catch (RuntimeException ignored) { } } chain.doFilter(request, response); } }
