package com.pavan.ai_resume_reviewer.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_drive_id", nullable = false)
    private JobDrive jobDrive;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    private String status; // e.g., APPLIED, SHORTLISTED, REJECTED

    private Double aiMatchScore;

    @Column(columnDefinition = "TEXT")
    private String aiFeedback;

    private LocalDateTime applicationDate;

    public JobApplication() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public JobDrive getJobDrive() { return jobDrive; }
    public void setJobDrive(JobDrive jobDrive) { this.jobDrive = jobDrive; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getAiMatchScore() { return aiMatchScore; }
    public void setAiMatchScore(Double aiMatchScore) { this.aiMatchScore = aiMatchScore; }

    public String getAiFeedback() { return aiFeedback; }
    public void setAiFeedback(String aiFeedback) { this.aiFeedback = aiFeedback; }

    public LocalDateTime getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDateTime applicationDate) { this.applicationDate = applicationDate; }
}
