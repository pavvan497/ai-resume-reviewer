package com.pavan.ai_resume_reviewer.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student_profiles")
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;

    private String branch;

    private Double cgpa;

    private Integer graduationYear;

    private String skills;

    private String resumeFileName; //->know which resume was uploaded
    private Double resumeAtsScore; //->store the AI-generated score

    @Column(columnDefinition = "Text")
    private String extractedSkills; //->later use the skills for recruiter -> student matching

    @Column(columnDefinition = "TEXT")
    private String aiFeedback;   //-> preserve geminis analysis

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public StudentProfile() {
    }

    public Long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Double getCgpa() {
        return cgpa;
    }

    public void setCgpa(Double cgpa) {
        this.cgpa = cgpa;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
    }

    public Double getResumeAtsScore() {
        return resumeAtsScore;
    }

    public void setResumeAtsScore(Double resumeAtsScore) {
        this.resumeAtsScore = resumeAtsScore;
    }

    public String getExtractedSkills() {
        return extractedSkills;
    }

    public void setExtractedSkills(String extractedSkills) {
        this.extractedSkills = extractedSkills;
    }

    public String getAiFeedback() {
        return aiFeedback;
    }

    public void setAiFeedback(String aiFeedback) {
        this.aiFeedback = aiFeedback;
    }
}