package com.pavan.ai_resume_reviewer.service;

import com.pavan.ai_resume_reviewer.dto.JobApplicationResponse;
import com.pavan.ai_resume_reviewer.entity.JobApplication;
import com.pavan.ai_resume_reviewer.entity.JobDrive;
import com.pavan.ai_resume_reviewer.entity.StudentProfile;
import com.pavan.ai_resume_reviewer.entity.User;
import com.pavan.ai_resume_reviewer.model.AiMatchResult;
import com.pavan.ai_resume_reviewer.repository.JobApplicationRepository;
import com.pavan.ai_resume_reviewer.repository.JobDriveRepository;
import com.pavan.ai_resume_reviewer.repository.StudentProfileRepository;
import com.pavan.ai_resume_reviewer.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobDriveRepository jobDriveRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;
    private final AiService aiService;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository,
                                 JobDriveRepository jobDriveRepository,
                                 StudentProfileRepository studentProfileRepository,
                                 UserRepository userRepository,
                                 AiService aiService) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobDriveRepository = jobDriveRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.userRepository = userRepository;
        this.aiService = aiService;
    }

    public JobApplicationResponse applyForJob(String studentEmail, Long jobDriveId) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentProfile profile = studentProfileRepository.findByUser(student)
                .orElseThrow(() -> new RuntimeException("Student profile not found. Please complete your profile first."));

        JobDrive jobDrive = jobDriveRepository.findById(jobDriveId)
                .orElseThrow(() -> new RuntimeException("Job Drive not found"));

        if (!jobDrive.isActive()) {
            throw new RuntimeException("This job drive is no longer active.");
        }

        if (jobApplicationRepository.findByStudentAndJobDrive(student, jobDrive).isPresent()) {
            throw new RuntimeException("You have already applied for this job.");
        }

        // --- RULE ENGINE: ELIGIBILITY CHECKS ---
        if (jobDrive.getMinimumCgpa() != null && profile.getCgpa() != null) {
            if (profile.getCgpa() < jobDrive.getMinimumCgpa()) {
                throw new RuntimeException("Not eligible: CGPA is below the minimum required (" + jobDrive.getMinimumCgpa() + ")");
            }
        }

        if (jobDrive.getGraduationYear() != null && profile.getGraduationYear() != null) {
            if (!profile.getGraduationYear().equals(jobDrive.getGraduationYear())) {
                throw new RuntimeException("Not eligible: Graduation year mismatch");
            }
        }

        if (jobDrive.getEligibleBranches() != null && !jobDrive.getEligibleBranches().isBlank()) {
            if (profile.getBranch() == null || !jobDrive.getEligibleBranches().toLowerCase().contains(profile.getBranch().toLowerCase())) {
                throw new RuntimeException("Not eligible: Your branch is not eligible for this drive");
            }
        }

        // --- AI MATCHMAKING ---
        String combinedSkills = (profile.getSkills() != null ? profile.getSkills() : "") + 
                                ", " + 
                                (profile.getExtractedSkills() != null ? profile.getExtractedSkills() : "");
                                
        String jobContext = jobDrive.getTitle() + " - " + jobDrive.getRequiredSkills() + " - " + jobDrive.getDescription();
        
        AiMatchResult matchResult = aiService.matchStudentToJob(combinedSkills, jobContext);

        // --- SAVE APPLICATION ---
        JobApplication application = new JobApplication();
        application.setStudent(student);
        application.setJobDrive(jobDrive);
        application.setStatus("APPLIED");
        application.setApplicationDate(LocalDateTime.now());
        application.setAiMatchScore(matchResult.getMatchScore());
        application.setAiFeedback(matchResult.getFeedback());

        JobApplication saved = jobApplicationRepository.save(application);
        return mapToResponse(saved);
    }

    public List<JobApplicationResponse> getMyApplications(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return jobApplicationRepository.findByStudent(student).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<JobApplicationResponse> getApplicationsForJob(Long jobDriveId) {
        JobDrive jobDrive = jobDriveRepository.findById(jobDriveId)
                .orElseThrow(() -> new RuntimeException("Job Drive not found"));
        return jobApplicationRepository.findByJobDrive(jobDrive).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private JobApplicationResponse mapToResponse(JobApplication application) {
        return new JobApplicationResponse(
                application.getId(),
                application.getJobDrive().getId(),
                application.getJobDrive().getTitle(),
                application.getJobDrive().getCompany().getName(),
                application.getStudent().getName(),
                application.getStudent().getEmail(),
                application.getStatus(),
                application.getAiMatchScore(),
                application.getAiFeedback(),
                application.getApplicationDate()
        );
    }
}
