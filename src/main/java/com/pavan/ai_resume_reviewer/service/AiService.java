package com.pavan.ai_resume_reviewer.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder builder ){
           this.chatClient= builder.build();
    }

    public String reviewResume(String resume, String jobDescription) {

        return chatClient
                .prompt()
                .system("""
                    You are an expert technical recruiter and resume reviewer.

                    Your job is to compare a candidate's resume with a job description.

                    Give honest, practical and constructive feedback.
                    Do not invent information that is not present in the resume.
                    """)
                .user(user -> user
                        .text("""
                            Analyze the candidate's resume against the job description.

                            RESUME:
                            {resume}

                            JOB DESCRIPTION:
                            {jobDescription}

                            Provide the following:

                            1. Matching skills
                            2. Missing skills
                            3. Resume strengths
                            4. Resume weaknesses
                            5. Project improvement suggestions
                            6. ATS improvement suggestions
                            7. Overall suitability for the job

                            Be specific and concise.
                            """)
                        .param("resume", resume)
                        .param("jobDescription", jobDescription))
                .call()
                .content();
    }

}
