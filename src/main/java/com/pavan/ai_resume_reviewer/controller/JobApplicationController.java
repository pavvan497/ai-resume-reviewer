package com.pavan.ai_resume_reviewer.controller;

import com.pavan.ai_resume_reviewer.dto.ApiResponse;
import com.pavan.ai_resume_reviewer.dto.JobApplicationResponse;
import com.pavan.ai_resume_reviewer.service.JobApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping("/apply/{jobDriveId}")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> applyForJob(@PathVariable Long jobDriveId, Authentication authentication) {
        try {
            String email = authentication.getName();
            JobApplicationResponse response = jobApplicationService.applyForJob(email, jobDriveId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Successfully applied for the job", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/my-applications")
    public ResponseEntity<ApiResponse<List<JobApplicationResponse>>> getMyApplications(Authentication authentication) {
        String email = authentication.getName();
        List<JobApplicationResponse> responses = jobApplicationService.getMyApplications(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "Fetched your applications", responses));
    }

    // Only recruiters should see this ideally, but keeping it open for testing
    @GetMapping("/job/{jobDriveId}")
    public ResponseEntity<ApiResponse<List<JobApplicationResponse>>> getApplicationsForJob(@PathVariable Long jobDriveId) {
        List<JobApplicationResponse> responses = jobApplicationService.getApplicationsForJob(jobDriveId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Fetched applications for job drive", responses));
    }
}
