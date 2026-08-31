package com.pavan.ai_resume_reviewer.controller;

import com.pavan.ai_resume_reviewer.model.AdviceRequest;
import com.pavan.ai_resume_reviewer.model.ApiResponse;
import com.pavan.ai_resume_reviewer.model.ResumeReview;
import com.pavan.ai_resume_reviewer.service.AiService;
import com.pavan.ai_resume_reviewer.service.PdfService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.pavan.ai_resume_reviewer.model.ResumeReviewRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    @PostMapping("/review-pdf")
    public ApiResponse<ResumeReview> reviewPdf(
            @RequestPart("resume") MultipartFile resume,
            @Valid @ModelAttribute ResumeReviewRequest request)
            throws IOException {

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

        if (resume.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    "Resume file size must not exceed 5 MB"
            );
        }

        String resumeText =
                pdfService.extractText(resume);

        return new ApiResponse<>(
                true,
                "Resume reviewed successfully",
                aiService.reviewResume(
                        resumeText,
                        request.getJobDescription(),
                        request.getConversationId()
                )
        );
    }



    // Follow-up advice using conversation memory
    @PostMapping("/advice")
    public ApiResponse<String> getAdvice(@Valid @RequestBody AdviceRequest request) {

        String advice= aiService.getAdvice(
                request.getQuestion(),
                request.getConversationId()
        );

        return new ApiResponse<>(
                true,
                "Advice generated succesfully",
                advice
        );
    }
}

