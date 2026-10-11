package com.pavan.ai_resume_reviewer.service;

import com.pavan.ai_resume_reviewer.dto.AdminDashboardResponse;
import com.pavan.ai_resume_reviewer.dto.JobApplicationResponse;
import com.pavan.ai_resume_reviewer.repository.CompanyRepository;
import com.pavan.ai_resume_reviewer.repository.JobApplicationRepository;
import com.pavan.ai_resume_reviewer.repository.JobDriveRepository;
import com.pavan.ai_resume_reviewer.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final JobDriveRepository jobDriveRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public AdminService(UserRepository userRepository,
                        CompanyRepository companyRepository,
                        JobDriveRepository jobDriveRepository,
                        JobApplicationRepository jobApplicationRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.jobDriveRepository = jobDriveRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    public AdminDashboardResponse getDashboardStats() {
        long students = userRepository.countByRole("STUDENT");
        long recruiters = userRepository.countByRole("RECRUITER");
        long companies = companyRepository.count();
        long activeJobs = jobDriveRepository.countByIsActiveTrue();
        long applications = jobApplicationRepository.count();

        // Get recent applications (limit to latest 10)
        List<JobApplicationResponse> recentApplications = jobApplicationRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getApplicationDate().compareTo(a.getApplicationDate()))
                .limit(10)
                .map(app -> new JobApplicationResponse(
                        app.getId(),
                        app.getJobDrive().getId(),
                        app.getJobDrive().getTitle(),
                        app.getJobDrive().getCompany().getName(),
                        app.getStudent().getName(),
                        app.getStudent().getEmail(),
                        app.getStatus(),
                        app.getAiMatchScore(),
                        app.getAiFeedback(),
                        app.getApplicationDate()
                ))
                .collect(Collectors.toList());

        return new AdminDashboardResponse(
                students,
                recruiters,
                companies,
                activeJobs,
                applications,
                recentApplications
        );
    }
}
