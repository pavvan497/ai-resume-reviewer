package com.pavan.ai_resume_reviewer.controller;

import com.pavan.ai_resume_reviewer.dto.ApiResponse;
import com.pavan.ai_resume_reviewer.dto.JobDriveRequest;
import com.pavan.ai_resume_reviewer.dto.JobDriveResponse;
import com.pavan.ai_resume_reviewer.service.JobDriveService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobDriveController {

    private final JobDriveService jobDriveService;

    public JobDriveController(JobDriveService jobDriveService) {
        this.jobDriveService = jobDriveService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<JobDriveResponse>> createJob(
            @Valid @RequestBody JobDriveRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        JobDriveResponse response = jobDriveService.createJobDrive(email, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Job Drive posted successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobDriveResponse>>> getAllActiveJobs() {
        List<JobDriveResponse> jobs = jobDriveService.getAllActiveJobs();
        return ResponseEntity.ok(new ApiResponse<>(true, "Active jobs retrieved", jobs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobDriveResponse>> getJobById(@PathVariable Long id) {
        JobDriveResponse job = jobDriveService.getJobDriveById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Job retrieved", job));
    }
}
