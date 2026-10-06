package com.pavan.ai_resume_reviewer.service;

import com.pavan.ai_resume_reviewer.entity.StudentProfile;
import com.pavan.ai_resume_reviewer.entity.User;
import com.pavan.ai_resume_reviewer.repository.StudentProfileRepository;
import com.pavan.ai_resume_reviewer.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;

    public StudentProfileService(
            StudentProfileRepository studentProfileRepository,
            UserRepository userRepository) {

        this.studentProfileRepository = studentProfileRepository;
        this.userRepository = userRepository;
    }

    public StudentProfile createProfile(
            String email,
            String phone,
            String branch,
            Double cgpa,
            Integer graduationYear,
            String skills) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (studentProfileRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("Profile already exists");
        }

        StudentProfile profile = new StudentProfile();
        profile.setUser(user);
        profile.setPhone(phone);
        profile.setBranch(branch);
        profile.setCgpa(cgpa);
        profile.setGraduationYear(graduationYear);
        profile.setSkills(skills);

        return studentProfileRepository.save(profile);
    }

    public com.pavan.ai_resume_reviewer.dto.StudentProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        StudentProfile profile = studentProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return new com.pavan.ai_resume_reviewer.dto.StudentProfileResponse(
                profile.getId(),
                user.getName(),
                user.getEmail(),
                profile.getPhone(),
                profile.getBranch(),
                profile.getCgpa(),
                profile.getGraduationYear(),
                profile.getSkills(),
                profile.getResumeFileName(),
                profile.getResumeAtsScore(),
                profile.getExtractedSkills(),
                profile.getAiFeedback()
        );
    }

    public com.pavan.ai_resume_reviewer.dto.StudentProfileResponse updateProfile(
            String email,
            com.pavan.ai_resume_reviewer.dto.StudentProfileRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        StudentProfile profile = studentProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setPhone(request.getPhone());
        profile.setBranch(request.getBranch());
        profile.setCgpa(request.getCgpa());
        profile.setGraduationYear(request.getGraduationYear());
        profile.setSkills(request.getSkills());

        studentProfileRepository.save(profile);

        return new com.pavan.ai_resume_reviewer.dto.StudentProfileResponse(
                profile.getId(),
                user.getName(),
                user.getEmail(),
                profile.getPhone(),
                profile.getBranch(),
                profile.getCgpa(),
                profile.getGraduationYear(),
                profile.getSkills(),
                profile.getResumeFileName(),
                profile.getResumeAtsScore(),
                profile.getExtractedSkills(),
                profile.getAiFeedback()
        );
    }

    public com.pavan.ai_resume_reviewer.dto.StudentProfileResponse updateResumeAnalysis(
            String email,
            String fileName,
            Double atsScore,
            String extractedSkills,
            String aiFeedback) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        StudentProfile profile = studentProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setResumeFileName(fileName);
        profile.setResumeAtsScore(atsScore);
        profile.setExtractedSkills(extractedSkills);
        profile.setAiFeedback(aiFeedback);

        studentProfileRepository.save(profile);

        return new com.pavan.ai_resume_reviewer.dto.StudentProfileResponse(
                profile.getId(),
                user.getName(),
                user.getEmail(),
                profile.getPhone(),
                profile.getBranch(),
                profile.getCgpa(),
                profile.getGraduationYear(),
                profile.getSkills(),
                profile.getResumeFileName(),
                profile.getResumeAtsScore(),
                profile.getExtractedSkills(),
                profile.getAiFeedback()
        );
    }
}