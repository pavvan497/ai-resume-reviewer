package com.pavan.ai_resume_reviewer.dto;

public class CompanyResponse {

    private Long id;
    private String name;
    private String description;
    private String website;
    private String industry;

    public CompanyResponse(Long id, String name, String description, String website, String industry) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.website = website;
        this.industry = industry;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getWebsite() { return website; }
    public String getIndustry() { return industry; }
}
