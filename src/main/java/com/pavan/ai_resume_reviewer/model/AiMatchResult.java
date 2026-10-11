package com.pavan.ai_resume_reviewer.model;

public class AiMatchResult {
    
    private Double matchScore;
    private String feedback;

    public AiMatchResult() {}

    public Double getMatchScore() { return matchScore; }
    public void setMatchScore(Double matchScore) { this.matchScore = matchScore; }
    
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}
