package com.pavan.ai_resume_reviewer.service;

import com.pavan.ai_resume_reviewer.dto.RecruiterProfileRequest;
import com.pavan.ai_resume_reviewer.dto.RecruiterProfileResponse;
import com.pavan.ai_resume_reviewer.entity.Company;
import com.pavan.ai_resume_reviewer.entity.RecruiterProfile;
import com.pavan.ai_resume_reviewer.entity.User;
import com.pavan.ai_resume_reviewer.repository.CompanyRepository;
import com.pavan.ai_resume_reviewer.repository.RecruiterProfileRepository;
import com.pavan.ai_resume_reviewer.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository,
                                   UserRepository userRepository,
                                   CompanyRepository companyRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    public RecruiterProfileResponse createProfile(String email, RecruiterProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (recruiterProfileRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("Recruiter profile already exists");
        }

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        RecruiterProfile profile = new RecruiterProfile();
        profile.setUser(user);
        profile.setCompany(company);
        profile.setDesignation(request.getDesignation());
        profile.setPhone(request.getPhone());

        RecruiterProfile saved = recruiterProfileRepository.save(profile);
        return mapToResponse(saved, user, company);
    }

    public RecruiterProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RecruiterProfile profile = recruiterProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));

        return mapToResponse(profile, user, profile.getCompany());
    }

    public RecruiterProfileResponse updateProfile(String email, RecruiterProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RecruiterProfile profile = recruiterProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        profile.setDesignation(request.getDesignation());
        profile.setPhone(request.getPhone());
        profile.setCompany(company);

        RecruiterProfile saved = recruiterProfileRepository.save(profile);
        return mapToResponse(saved, user, company);
    }

    private RecruiterProfileResponse mapToResponse(RecruiterProfile profile, User user, Company company) {
        return new RecruiterProfileResponse(
                profile.getId(),
                user.getName(),
                user.getEmail(),
                profile.getDesignation(),
                profile.getPhone(),
                company.getName(),
                company.getId()
        );
    }
}
