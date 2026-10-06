package com.pavan.ai_resume_reviewer.dto;

import jakarta.validation.constraints.NotBlank;

public class CompanyRequest {

    @NotBlank(message = "Company name is required")
    private String name;
    private String description;
    private String website;
    private String industry;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
}
