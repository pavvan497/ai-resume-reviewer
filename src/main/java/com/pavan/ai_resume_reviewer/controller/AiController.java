package com.pavan.ai_resume_reviewer.controller;

import com.pavan.ai_resume_reviewer.model.ResumeReview;
import com.pavan.ai_resume_reviewer.service.AiService;
import com.pavan.ai_resume_reviewer.service.PdfService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class AiController {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private final AiService aiService;
    private final PdfService pdfService;

    public AiController(
            AiService aiService,
            PdfService pdfService) {

        this.aiService = aiService;
        this.pdfService = pdfService;
    }

    // Simple text-based resume review
    @GetMapping("/review")
    public ResumeReview review(
            @RequestParam String resume,
            @RequestParam String jobDescription) {

        return aiService.reviewResume(
                resume,
                jobDescription
        );
    }

    // Extract text from PDF
    @PostMapping("/extract")
    public String extractResume(
            @RequestParam("resume") MultipartFile resume)
            throws IOException {

        return pdfService.extractText(resume);
    }

    // PDF resume review with conversation memory
    @PostMapping("/review-pdf")
    public ResumeReview reviewPdf(
            @RequestPart("resume") MultipartFile resume,
            @RequestPart("jobDescription") String jobDescription,
            @RequestParam("conversationId") String conversationId)
            throws IOException {

        System.out.println(
                "Conversation ID received: " + conversationId
        );

        if (conversationId == null || conversationId.isBlank()) {
            throw new IllegalArgumentException(
                    "conversationId cannot be empty"
            );
        }

        if (resume.isEmpty()) {
            throw new IllegalArgumentException(
                    "Resume file cannot be empty"
            );
        }

        if (resume.getOriginalFilename() == null ||
                !resume.getOriginalFilename()
                        .toLowerCase()
                        .endsWith(".pdf")) {

            throw new IllegalArgumentException(
                    "Only PDF files are allowed"
            );
        }

        if (jobDescription == null ||
                jobDescription.isBlank()) {

            throw new IllegalArgumentException(
                    "Job description cannot be empty"
            );
        }

        if (resume.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    "Resume file size must not exceed 5 MB"
            );
        }

        String resumeText =
                pdfService.extractText(resume);

        return aiService.reviewResume(
                resumeText,
                jobDescription,
                conversationId
        );
    }

    // Follow-up advice using conversation memory
    @GetMapping("/advice")
    public String getAdvice(
            @RequestParam String question,
            @RequestParam String conversationId) {

        System.out.println(
                "Advice conversation ID: " + conversationId
        );

        return aiService.getAdvice(
                question,
                conversationId
        );
    }
}

