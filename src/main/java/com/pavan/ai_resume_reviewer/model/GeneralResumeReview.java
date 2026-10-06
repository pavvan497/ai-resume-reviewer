package com.pavan.ai_resume_reviewer.model;

import java.util.List;

public class GeneralResumeReview {

    private double atsScore;
    private List<String> extractedSkills;
    private String aiFeedback;

    public GeneralResumeReview() {
    }

    public double getAtsScore() {
        return atsScore;
    }

    public void setAtsScore(double atsScore) {
        this.atsScore = atsScore;
    }

    public List<String> getExtractedSkills() {
        return extractedSkills;
    }

    public void setExtractedSkills(List<String> extractedSkills) {
        this.extractedSkills = extractedSkills;
    }

    public String getAiFeedback() {
        return aiFeedback;
    }

    public void setAiFeedback(String aiFeedback) {
        this.aiFeedback = aiFeedback;
    }
}
