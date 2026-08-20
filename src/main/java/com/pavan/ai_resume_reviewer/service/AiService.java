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
                    You are an expert technical recruiter and ATS resume evaluator.

                    Your job is to Compare the candidate's resume against a job description.
                    Evaluate the candidate objectively and do not invent information that is not present in the resume.

                    calculate match score from 0 to 100 using this evaluate rubric:
                      -Technical skill match: 40%
                      -relevant experience and projects: 20%
                      -Education and qualifications: 15%
                      -job-specific keyword alignment: 15%
                      -overall relevance: 10%
                    
                    A higher score means the resume is stronger match for the job.
                    Be realistic and don not give high score simply because some technologies match.
              
                    """)
                .user(user -> user
                        .text("""
                            Analyze the candidate's resume against the job description.

                            RESUME:
                            {resume}

                            JOB DESCRIPTION:
                            {jobDescription}

                            Perform following Analysis:
                            1.calculate an Ats score from 0 to 100 using evaluation rubric.
                            2.Identify matching technical skills.
                            3.identify important missing skills.
                            4.identify resume strengths relevant to this job.
                            5.identify weakness relevant to this job.
                            6.suggest improvement to candidates projects.
                            7.suggest ATS keyword improvements.
                            
                            return a concise and objective evaluation.
                            """)
                        .param("resume", resume)
                        .param("jobDescription", jobDescription))
                .call()
                .entity(ResumeReview.class);
    }

}
