package com.talentsync.profileservice.controller;

import com.talentsync.profileservice.dto.*;
import com.talentsync.profileservice.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController @RequestMapping("/api/profiles") @RequiredArgsConstructor
public class ProfileController {
    private final ProfileService service;
    @PostMapping public ResponseEntity<ProfileResponse> create(@Valid @RequestBody ProfileRequest request) { ProfileResponse p = service.create(request); return ResponseEntity.created(URI.create("/api/profiles/" + p.getUserId())).body(p); }
    @GetMapping("/{userId}") public ProfileResponse get(@PathVariable Long userId) { return service.getByUser(userId); }
    @PutMapping("/{userId}") public ProfileResponse update(@PathVariable Long userId, @Valid @RequestBody ProfileRequest request) { return service.update(userId, request); }
    @DeleteMapping("/{userId}") public ResponseEntity<Void> delete(@PathVariable Long userId) { service.delete(userId); return ResponseEntity.noContent().build(); }
}
