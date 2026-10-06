package com.pavan.ai_resume_reviewer.service;

import com.pavan.ai_resume_reviewer.dto.CompanyRequest;
import com.pavan.ai_resume_reviewer.dto.CompanyResponse;
import com.pavan.ai_resume_reviewer.entity.Company;
import com.pavan.ai_resume_reviewer.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public CompanyResponse createCompany(CompanyRequest request) {
        if (companyRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException("Company with this name already exists");
        }

        Company company = new Company();
        company.setName(request.getName());
        company.setDescription(request.getDescription());
        company.setWebsite(request.getWebsite());
        company.setIndustry(request.getIndustry());

        Company saved = companyRepository.save(company);
        return mapToResponse(saved);
    }

    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CompanyResponse getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return mapToResponse(company);
    }

    private CompanyResponse mapToResponse(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getDescription(),
                company.getWebsite(),
                company.getIndustry()
        );
    }
}
