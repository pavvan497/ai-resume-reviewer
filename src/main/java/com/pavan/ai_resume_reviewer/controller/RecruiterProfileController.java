package com.pavan.ai_resume_reviewer.controller;

import com.pavan.ai_resume_reviewer.dto.ApiResponse;
import com.pavan.ai_resume_reviewer.dto.RecruiterProfileRequest;
import com.pavan.ai_resume_reviewer.dto.RecruiterProfileResponse;
import com.pavan.ai_resume_reviewer.service.RecruiterProfileService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recruiter/profile")
public class RecruiterProfileController {

    private final RecruiterProfileService recruiterProfileService;

    public RecruiterProfileController(RecruiterProfileService recruiterProfileService) {
        this.recruiterProfileService = recruiterProfileService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RecruiterProfileResponse>> createProfile(
            @Valid @RequestBody RecruiterProfileRequest request,
            Authentication authentication) {
        
        String email = authentication.getName();
        RecruiterProfileResponse response = recruiterProfileService.createProfile(email, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Recruiter profile created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<RecruiterProfileResponse>> getProfile(Authentication authentication) {
        String email = authentication.getName();
        RecruiterProfileResponse response = recruiterProfileService.getProfile(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "Recruiter profile retrieved successfully", response));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<RecruiterProfileResponse>> updateProfile(
            @Valid @RequestBody RecruiterProfileRequest request,
            Authentication authentication) {
        
        String email = authentication.getName();
        RecruiterProfileResponse response = recruiterProfileService.updateProfile(email, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Recruiter profile updated successfully", response));
    }
}
