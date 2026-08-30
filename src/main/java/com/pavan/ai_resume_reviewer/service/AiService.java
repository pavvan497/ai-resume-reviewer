package com.pavan.ai_resume_reviewer.service;

import com.pavan.ai_resume_reviewer.model.ResumeReview;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(
            ChatClient.Builder builder,
            ChatMemory chatMemory) {

        this.chatClient = builder
                .defaultAdvisors(
                        org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
                                .builder(chatMemory)
                                .build()
                )
                .build();
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
                .advisors(advisor -> advisor
                        .param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
}
