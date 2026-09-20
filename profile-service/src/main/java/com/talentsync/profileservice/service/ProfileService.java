package com.talentsync.profileservice.service;

import com.talentsync.profileservice.dto.*;
import com.talentsync.profileservice.entity.Profile;
import com.talentsync.profileservice.exception.ProfileExceptions;
import com.talentsync.profileservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Transactional
public class ProfileService {
    private final ProfileRepository repository;
    public ProfileResponse create(ProfileRequest request) { if (repository.existsByUserId(request.getUserId())) throw new ProfileExceptions.Conflict(request.getUserId()); return toResponse(repository.save(copy(request, new Profile()))); }
    @Transactional(readOnly = true) public ProfileResponse getByUser(Long userId) { return repository.findByUserId(userId).map(this::toResponse).orElseThrow(() -> new ProfileExceptions.NotFound(userId)); }
    public ProfileResponse update(Long userId, ProfileRequest request) { Profile profile = repository.findByUserId(userId).orElseThrow(() -> new ProfileExceptions.NotFound(userId)); if (!userId.equals(request.getUserId())) throw new IllegalArgumentException("Profile userId cannot be changed"); return toResponse(repository.save(copy(request, profile))); }
    public void delete(Long userId) { Profile profile = repository.findByUserId(userId).orElseThrow(() -> new ProfileExceptions.NotFound(userId)); repository.delete(profile); }
    private Profile copy(ProfileRequest r, Profile p) { p.setUserId(r.getUserId()); p.setRole(r.getRole().trim().toUpperCase()); if (!p.getRole().equals("CANDIDATE") && !p.getRole().equals("RECRUITER")) throw new IllegalArgumentException("Role must be CANDIDATE or RECRUITER"); p.setFullName(r.getFullName()); p.setEmail(r.getEmail()); p.setPhone(r.getPhone()); p.setLocation(r.getLocation()); p.setBio(r.getBio()); p.setSkills(r.getSkills()); p.setCompany(r.getCompany()); p.setResumeUrl(r.getResumeUrl()); return p; }
    private ProfileResponse toResponse(Profile p) { return new ProfileResponse(p.getId(), p.getUserId(), p.getRole(), p.getFullName(), p.getEmail(), p.getPhone(), p.getLocation(), p.getBio(), p.getSkills(), p.getCompany(), p.getResumeUrl(), p.getCreatedAt(), p.getUpdatedAt()); }
}
