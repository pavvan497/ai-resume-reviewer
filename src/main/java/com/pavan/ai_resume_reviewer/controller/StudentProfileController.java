package com.pavan.ai_resume_reviewer.controller;

import com.pavan.ai_resume_reviewer.dto.StudentProfileRequest;
import com.pavan.ai_resume_reviewer.service.StudentProfileService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student/profile")
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    public StudentProfileController(StudentProfileService studentProfileService) {
        this.studentProfileService = studentProfileService;
    }

    @PostMapping
    public org.springframework.http.ResponseEntity<com.pavan.ai_resume_reviewer.dto.ApiResponse<com.pavan.ai_resume_reviewer.dto.StudentProfileResponse>> createProfile(
            @Valid @RequestBody StudentProfileRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        studentProfileService.createProfile(
                email,
                request.getPhone(),
                request.getBranch(),
                request.getCgpa(),
                request.getGraduationYear(),
                request.getSkills()
        );

        com.pavan.ai_resume_reviewer.dto.StudentProfileResponse response = studentProfileService.getProfile(email);

        return org.springframework.http.ResponseEntity.ok(
                new com.pavan.ai_resume_reviewer.dto.ApiResponse<>(true, "Student profile created successfully", response)
        );
    }

    @GetMapping
    public org.springframework.http.ResponseEntity<com.pavan.ai_resume_reviewer.dto.ApiResponse<com.pavan.ai_resume_reviewer.dto.StudentProfileResponse>> getProfile(
            Authentication authentication) {
        String email = authentication.getName();
        com.pavan.ai_resume_reviewer.dto.StudentProfileResponse response = studentProfileService.getProfile(email);
        return org.springframework.http.ResponseEntity.ok(
                new com.pavan.ai_resume_reviewer.dto.ApiResponse<>(true, "Profile retrieved successfully", response)
        );
    }

    @PutMapping
    public org.springframework.http.ResponseEntity<com.pavan.ai_resume_reviewer.dto.ApiResponse<com.pavan.ai_resume_reviewer.dto.StudentProfileResponse>> updateProfile(
            @Valid @RequestBody StudentProfileRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        com.pavan.ai_resume_reviewer.dto.StudentProfileResponse response = studentProfileService.updateProfile(email, request);
        return org.springframework.http.ResponseEntity.ok(
                new com.pavan.ai_resume_reviewer.dto.ApiResponse<>(true, "Profile updated successfully", response)
        );
    }
}