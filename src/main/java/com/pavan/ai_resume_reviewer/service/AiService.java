package com.pavan.ai_resume_reviewer.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder builder ){
           this.chatClient= builder.build();
    }

    public String reviewResume(String resume) {

        return chatClient
                .prompt()
                .system("""
                    You are an expert technical recruiter and resume reviewer.
                    Your job is to analyze resumes for software engineering roles.
                    Give honest, practical and constructive feedback.
                    Focus on technical skills, projects, experience, education
                    and ATS compatibility.
                    Do not invent information that is not present in the resume.
                    """)
                .user(user -> user
                        .text("""
                            Review the following resume.

                            RESUME:
                            {resume}

                            Provide:
                            1. Strengths
                            2. Weaknesses
                            3. Missing skills
                            4. Project improvements
                            5. ATS suggestions
                            """)
                        .param("resume", resume))
                .call()
                .content();
    }

}
