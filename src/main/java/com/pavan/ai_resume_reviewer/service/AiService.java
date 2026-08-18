package com.pavan.ai_resume_reviewer.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import com.pavan.ai_resume_reviewer.model.ResumeReview;

@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder builder ){
           this.chatClient= builder.build();
    }

    public ResumeReview reviewResume(String resume, String jobDescription) {

        return chatClient
                .prompt()
                .system("""
                    You are an expert technical recruiter and resume reviewer.

                    Compare the candidate's resume with the job description.

                    Return the analysis in the requested structured format.
                    Do not invent information that is not present in the resume.
                    """)
                .user(user -> user
                        .text("""
                            Analyze the candidate's resume against the job description.

                            RESUME:
                            {resume}

                            JOB DESCRIPTION:
                            {jobDescription}

                            Analyze:

                            - Match score from 0 to 100
                            - Matching skills
                            - Missing skills
                            - Resume strengths
                            - Resume weaknesses
                            - Project improvement suggestions
                            - ATS improvement suggestions
                            """)
                        .param("resume", resume)
                        .param("jobDescription", jobDescription))
                .call()
                .entity(ResumeReview.class);
    }

}
