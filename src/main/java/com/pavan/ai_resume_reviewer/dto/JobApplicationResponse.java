package com.pavan.ai_resume_reviewer.dto;

import java.time.LocalDateTime;

public class JobApplicationResponse {

    private Long id;
    private Long jobDriveId;
    private String jobTitle;
    private String companyName;
    private String studentName;
    private String studentEmail;
    
    private String status;
    private Double aiMatchScore;
    private String aiFeedback;
    private LocalDateTime applicationDate;

    public JobApplicationResponse(Long id, Long jobDriveId, String jobTitle, String companyName, String studentName, String studentEmail, String status, Double aiMatchScore, String aiFeedback, LocalDateTime applicationDate) {
        this.id = id;
        this.jobDriveId = jobDriveId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.status = status;
        this.aiMatchScore = aiMatchScore;
        this.aiFeedback = aiFeedback;
        this.applicationDate = applicationDate;
    }

    public Long getId() { return id; }
    public Long getJobDriveId() { return jobDriveId; }
    public String getJobTitle() { return jobTitle; }
    public String getCompanyName() { return companyName; }
    public String getStudentName() { return studentName; }
    public String getStudentEmail() { return studentEmail; }
    public String getStatus() { return status; }
    public Double getAiMatchScore() { return aiMatchScore; }
    public String getAiFeedback() { return aiFeedback; }
    public LocalDateTime getApplicationDate() { return applicationDate; }
}
