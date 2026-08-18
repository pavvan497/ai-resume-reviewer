package com.pavan.ai_resume_reviewer.model;

import java.util.List;

public class ResumeReview {

    private int matchScore;
    private List<String> matchingSkills;
    private List<String> missingSkills;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> projectSuggestions;
    private List<String> atsSuggestions;

    public int getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(int matchScore) {
        this.matchScore = matchScore;
    }

    public List<String> getMatchingSkills() {
        return matchingSkills;
    }

    public void setMatchingSkills(List<String> matchingSkills) {
        this.matchingSkills = matchingSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(List<String> weaknesses) {
        this.weaknesses = weaknesses;
    }

    public List<String> getProjectSuggestions() {
        return projectSuggestions;
    }

    public void setProjectSuggestions(List<String> projectSuggestions) {
        this.projectSuggestions = projectSuggestions;
    }

    public List<String> getAtsSuggestions() {
        return atsSuggestions;
    }

    public void setAtsSuggestions(List<String> atsSuggestions) {
        this.atsSuggestions = atsSuggestions;
    }
}