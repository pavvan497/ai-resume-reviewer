package com.pavan.ai_resume_reviewer.service;

import com.pavan.ai_resume_reviewer.dto.JobDriveRequest;
import com.pavan.ai_resume_reviewer.dto.JobDriveResponse;
import com.pavan.ai_resume_reviewer.entity.JobDrive;
import com.pavan.ai_resume_reviewer.entity.RecruiterProfile;
import com.pavan.ai_resume_reviewer.entity.User;
import com.pavan.ai_resume_reviewer.repository.JobDriveRepository;
import com.pavan.ai_resume_reviewer.repository.RecruiterProfileRepository;
import com.pavan.ai_resume_reviewer.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobDriveService {

    private final JobDriveRepository jobDriveRepository;
    private final UserRepository userRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;

    public JobDriveService(JobDriveRepository jobDriveRepository, UserRepository userRepository, RecruiterProfileRepository recruiterProfileRepository) {
        this.jobDriveRepository = jobDriveRepository;
        this.userRepository = userRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    public JobDriveResponse createJobDrive(String recruiterEmail, JobDriveRequest request) {
        User user = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RecruiterProfile profile = recruiterProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found. Please create a profile first."));

        JobDrive job = new JobDrive();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setPackageDetails(request.getPackageDetails());
        job.setLocation(request.getLocation());
        job.setMinimumCgpa(request.getMinimumCgpa());
        job.setEligibleBranches(request.getEligibleBranches());
        job.setGraduationYear(request.getGraduationYear());
        job.setRequiredSkills(request.getRequiredSkills());
        job.setDeadline(request.getDeadline());
        
        job.setCompany(profile.getCompany());
        job.setPostedBy(user);

        JobDrive savedJob = jobDriveRepository.save(job);
        return mapToResponse(savedJob);
    }

    public List<JobDriveResponse> getAllActiveJobs() {
        return jobDriveRepository.findByIsActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public JobDriveResponse getJobDriveById(Long id) {
        JobDrive job = jobDriveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job Drive not found"));
        return mapToResponse(job);
    }

    private JobDriveResponse mapToResponse(JobDrive job) {
        return new JobDriveResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getPackageDetails(),
                job.getLocation(),
                job.getCompany().getName(),
                job.getMinimumCgpa(),
                job.getEligibleBranches(),
                job.getGraduationYear(),
                job.getRequiredSkills(),
                job.getDeadline(),
                job.isActive()
        );
    }
}
