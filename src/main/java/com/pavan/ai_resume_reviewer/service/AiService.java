package com.pavan.ai_resume_reviewer.service;

import com.pavan.ai_resume_reviewer.model.ResumeReview;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public AiService(
            ChatClient.Builder builder,
            ChatMemory chatMemory) {

        this.chatClient = builder.build();
        this.chatMemory = chatMemory;
    }

    // General resume analysis (no job description)
    public com.pavan.ai_resume_reviewer.model.GeneralResumeReview analyzeResume(String resumeText) {
        return chatClient
                .prompt()
                .system("""
                        You are an expert technical recruiter and ATS software analyzer.

                        Your job is to objectively analyze the candidate's resume on its own merits, without any specific job description.
                        Extract the key skills present, evaluate the formatting and general impact of the resume to generate a general ATS score (out of 100).
                        Provide a brief, constructive feedback summary.
                        
                        CRITICAL INSTRUCTION: Return ONLY raw, valid JSON. Do NOT wrap the response in ```json or any other markdown blocks.
                        """)
                .user(user -> user
                        .text("""
                                Analyze the following resume:
                                
                                {resume}
                                
                                Perform the following tasks:
                                1. Calculate a general ATS score from 0 to 100 based on standard ATS best practices (formatting, clarity, impact, buzzwords).
                                2. Extract a comprehensive list of technical skills found in the resume.
                                3. Provide concise overall feedback on how the candidate can improve their resume generally.
                                """)
                        .param("resume", resumeText))
                .call()
                .entity(com.pavan.ai_resume_reviewer.model.GeneralResumeReview.class);
    }

    // Existing normal resume review
    public ResumeReview reviewResume(
            String resume,
            String jobDescription) {

        return chatClient
                .prompt()
                .system("""
                        You are an expert technical recruiter and ATS resume evaluator.

                        Your job is to compare the candidate's resume against a job description.
                        Evaluate the candidate objectively and do not invent information that is not present in the resume.

                        Calculate match score from 0 to 100 using this evaluation rubric:
                        - Technical skill match: 40%
                        - Relevant experience and projects: 20%
                        - Education and qualifications: 15%
                        - Job-specific keyword alignment: 15%
                        - Overall relevance: 10%

                        A higher score means the resume is a stronger match for the job.
                        Be realistic and do not give a high score simply because some technologies match.
                        
                        CRITICAL INSTRUCTION: Return ONLY raw, valid JSON. Do NOT wrap the response in ```json or any other markdown blocks.
                        """)
                .user(user -> user
                        .text("""
                                Analyze the candidate's resume against the job description.

                                RESUME:
                                {resume}

                                JOB DESCRIPTION:
                                {jobDescription}

                                Perform the following analysis:
                                1. Calculate an ATS score from 0 to 100 using the evaluation rubric.
                                2. Identify matching technical skills.
                                3. Identify important missing skills.
                                4. Identify resume strengths relevant to this job.
                                5. Identify weaknesses relevant to this job.
                                6. Suggest improvements to the candidate's projects.
                                7. Suggest ATS keyword improvements.

                                Return a concise and objective evaluation.
                                """)
                        .param("resume", resume)
                        .param("jobDescription", jobDescription))
                .call()
                .entity(ResumeReview.class);
    }

    // Memory-enabled resume review
    public ResumeReview reviewResume(
            String resume,
            String jobDescription,
            String conversationId) {

        if (conversationId == null || conversationId.isBlank()) {
            throw new IllegalArgumentException(
                    "conversationId cannot be empty"
            );
        }

        return chatClient
                .prompt()
                .system("""
                        You are an expert technical recruiter and ATS resume evaluator.

                        Your job is to compare the candidate's resume against a job description.
                        Evaluate the candidate objectively and do not invent information that is not present in the resume.

                        Calculate match score from 0 to 100 using this evaluation rubric:
                        - Technical skill match: 40%
                        - Relevant experience and projects: 20%
                        - Education and qualifications: 15%
                        - Job-specific keyword alignment: 15%
                        - Overall relevance: 10%

                        A higher score means the resume is a stronger match for the job.
                        Be realistic and do not give a high score simply because some technologies match.
                        
                        CRITICAL INSTRUCTION: Return ONLY raw, valid JSON. Do NOT wrap the response in ```json or any other markdown blocks.
                        """)
                .user(user -> user
                        .text("""
                                Analyze the candidate's resume against the job description.

                                RESUME:
                                {resume}

                                JOB DESCRIPTION:
                                {jobDescription}

                                Perform the following analysis:
                                1. Calculate an ATS score from 0 to 100 using the evaluation rubric.
                                2. Identify matching technical skills.
                                3. Identify important missing skills.
                                4. Identify resume strengths relevant to this job.
                                5. Identify weaknesses relevant to this job.
                                6. Suggest improvements to the candidate's projects.
                                7. Suggest ATS keyword improvements.

                                Return a concise and objective evaluation.
                                """)
                        .param("resume", resume)
                        .param("jobDescription", jobDescription))
                .advisors(org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor.builder(chatMemory).build())
                .advisors(advisor -> advisor
                        .param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .entity(ResumeReview.class);
    }

    // Follow-up AI advice using conversation memory
    public String getAdvice(
            String question,
            String conversationId) {

        if (conversationId == null || conversationId.isBlank()) {
            throw new IllegalArgumentException(
                    "conversationId cannot be empty"
            );
        }

        return chatClient
                .prompt()
                .system("""
                        You are a career advisor for software engineering students.

                        Give practical and realistic advice.
                        Keep the explanation simple and beginner-friendly.
                        Do not invent information about the candidate.

                        Use the previous conversation context when answering.
                        """)
                .user(user -> user
                        .text("""
                                Answer the following question:

                                {question}
                                """)
                        .param("question", question))
                .advisors(org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor.builder(chatMemory).build())
                .advisors(advisor -> advisor
                        .param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
}
