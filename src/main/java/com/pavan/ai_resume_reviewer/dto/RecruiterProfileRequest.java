package com.pavan.ai_resume_reviewer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RecruiterProfileRequest {

    @NotBlank(message = "Designation is required")
    private String designation;
    
    @NotBlank(message = "Phone is required")
    private String phone;

    @NotNull(message = "Company ID is required")
    private Long companyId;

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
}
