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
    private final com.pavan.ai_resume_reviewer.service.AiService aiService;
    private final com.pavan.ai_resume_reviewer.service.PdfService pdfService;

    public StudentProfileController(
            StudentProfileService studentProfileService,
            com.pavan.ai_resume_reviewer.service.AiService aiService,
            com.pavan.ai_resume_reviewer.service.PdfService pdfService) {
        this.studentProfileService = studentProfileService;
        this.aiService = aiService;
        this.pdfService = pdfService;
    }

    @PostMapping("/resume")
    public org.springframework.http.ResponseEntity<com.pavan.ai_resume_reviewer.dto.ApiResponse<com.pavan.ai_resume_reviewer.dto.StudentProfileResponse>> uploadResume(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            Authentication authentication) {
        try {
            String email = authentication.getName();
            
            // 1. Extract text from PDF
            String resumeText = pdfService.extractText(file);
            
            if (resumeText == null || resumeText.trim().isEmpty()) {
                return org.springframework.http.ResponseEntity.badRequest().body(
                        new com.pavan.ai_resume_reviewer.dto.ApiResponse<>(false, "Failed to extract text from PDF. The file might be an image or corrupted.", null)
                );
            }
            
            // 2. Perform general AI analysis
            com.pavan.ai_resume_reviewer.model.GeneralResumeReview review = aiService.analyzeResume(resumeText);
            
            // 3. Format extracted skills into a single string for storage
            String skillsString = review.getExtractedSkills() != null 
                    ? String.join(", ", review.getExtractedSkills()) 
                    : "";
            
            // 4. Update the profile
            com.pavan.ai_resume_reviewer.dto.StudentProfileResponse response = studentProfileService.updateResumeAnalysis(
                    email,
                    file.getOriginalFilename(),
                    review.getAtsScore(),
                    skillsString,
                    review.getAiFeedback()
            );
            
            return org.springframework.http.ResponseEntity.ok(
                    new com.pavan.ai_resume_reviewer.dto.ApiResponse<>(true, "Resume uploaded and analyzed successfully", response)
            );
        } catch (Exception e) {
            e.printStackTrace();
            String rootCause = e.getCause() != null ? e.getCause().getMessage() : "No underlying cause";
            return org.springframework.http.ResponseEntity.badRequest().body(
                    new com.pavan.ai_resume_reviewer.dto.ApiResponse<>(false, "Failed to process resume: " + e.getMessage() + " | Root Cause: " + rootCause, null)
            );
        }
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