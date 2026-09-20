package com.talentsync.authservice.security;

import com.talentsync.authservice.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                String email = jwtService.username(token);
                userRepository.findByEmailIgnoreCase(email).ifPresent(account -> {
                    org.springframework.security.core.userdetails.UserDetails details = User.withUsername(account.getEmail()).password(account.getPasswordHash()).roles(account.getRole()).build();
                    if (jwtService.valid(token, details)) {
                        var authentication = new UsernamePasswordAuthenticationToken(details, null, List.of(new SimpleGrantedAuthority("ROLE_" + account.getRole())));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                });
            } catch (RuntimeException ignored) {
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}
