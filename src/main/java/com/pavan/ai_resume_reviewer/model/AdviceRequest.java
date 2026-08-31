package com.pavan.ai_resume_reviewer.model;

import jakarta.validation.constraints.NotBlank;

public class AdviceRequest {

    @NotBlank(message = "Question cannot be empty")
    private String question;

    @NotBlank(message = "ConversationId cannot be empty")
    private String conversationId;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
