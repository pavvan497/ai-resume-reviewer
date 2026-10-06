package com.pavan.ai_resume_reviewer.dto;

public class StudentProfileResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String branch;
    private Double cgpa;
    private Integer graduationYear;
    private String skills;

    private String resumeFileName;
    private Double resumeAtsScore;
    private String extractedSkills;
    private String aiFeedback;

    public StudentProfileResponse() {
    }

    public StudentProfileResponse(Long id, String name, String email, String phone, String branch, Double cgpa, Integer graduationYear, String skills, String resumeFileName, Double resumeAtsScore, String extractedSkills, String aiFeedback) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.branch = branch;
        this.cgpa = cgpa;
        this.graduationYear = graduationYear;
        this.skills = skills;
        this.resumeFileName = resumeFileName;
        this.resumeAtsScore = resumeAtsScore;
        this.extractedSkills = extractedSkills;
        this.aiFeedback = aiFeedback;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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