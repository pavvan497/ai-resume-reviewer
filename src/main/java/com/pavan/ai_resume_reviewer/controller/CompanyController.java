package com.pavan.ai_resume_reviewer.controller;

import com.pavan.ai_resume_reviewer.dto.ApiResponse;
import com.pavan.ai_resume_reviewer.dto.CompanyRequest;
import com.pavan.ai_resume_reviewer.dto.CompanyResponse;
import com.pavan.ai_resume_reviewer.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CompanyResponse>> createCompany(@Valid @RequestBody CompanyRequest request) {
        CompanyResponse response = companyService.createCompany(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Company created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CompanyResponse>>> getAllCompanies() {
        List<CompanyResponse> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(new ApiResponse<>(true, "Companies retrieved successfully", companies));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyResponse>> getCompanyById(@PathVariable Long id) {
        CompanyResponse company = companyService.getCompanyById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Company retrieved successfully", company));
    }
}
