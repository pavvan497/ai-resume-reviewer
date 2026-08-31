package com.pavan.ai_resume_reviewer.model;

import jakarta.validation.constraints.NotBlank;

public class ResumeReviewRequest {

    @NotBlank(message = "Job description cannot be empty")
    private String jobDescription;

    @NotBlank(message = "Conversation ID cannot be empty")
    private String conversationId;

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}

