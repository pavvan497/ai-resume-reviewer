package com.pavan.ai_resume_reviewer.dto;

import java.time.LocalDateTime;

public class JobDriveResponse {

    private Long id;
    private String title;
    private String description;
    private String packageDetails;
    private String location;
    private String companyName;
    
    private Double minimumCgpa;
    private String eligibleBranches;
    private Integer graduationYear;
    private String requiredSkills;
    private LocalDateTime deadline;
    private boolean isActive;

    public JobDriveResponse(Long id, String title, String description, String packageDetails, String location, String companyName, Double minimumCgpa, String eligibleBranches, Integer graduationYear, String requiredSkills, LocalDateTime deadline, boolean isActive) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.packageDetails = packageDetails;
        this.location = location;
        this.companyName = companyName;
        this.minimumCgpa = minimumCgpa;
        this.eligibleBranches = eligibleBranches;
        this.graduationYear = graduationYear;
        this.requiredSkills = requiredSkills;
        this.deadline = deadline;
        this.isActive = isActive;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPackageDetails() { return packageDetails; }
    public String getLocation() { return location; }
    public String getCompanyName() { return companyName; }
    public Double getMinimumCgpa() { return minimumCgpa; }
    public String getEligibleBranches() { return eligibleBranches; }
    public Integer getGraduationYear() { return graduationYear; }
    public String getRequiredSkills() { return requiredSkills; }
    public LocalDateTime getDeadline() { return deadline; }
    public boolean isActive() { return isActive; }
}
