package com.pavan.ai_resume_reviewer.controller;

import com.pavan.ai_resume_reviewer.model.ResumeReview;
import com.pavan.ai_resume_reviewer.service.AiService;
import com.pavan.ai_resume_reviewer.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.pavan.ai_resume_reviewer.model.ResumeReview;
import com.pavan.ai_resume_reviewer.service.PdfService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class AiController {


    private final  AiService aiService;
    private final PdfService pdfService;

    public AiController(AiService aiService, PdfService pdfService){
          this.aiService=aiService;
          this.pdfService=pdfService;
    }

    @GetMapping("/review")
    public ResumeReview review(@RequestParam String resume,
                               @RequestParam String jobDescription){
        return aiService.reviewResume(resume,jobDescription);
    }

    @PostMapping("/extract")
    public String extractResume(@RequestParam("resume") MultipartFile resume) throws IOException{

        return pdfService.extractText((resume));
    }

    @PostMapping("/review-pdf")
    public ResumeReview reviewPdf(
            @RequestPart("resume") MultipartFile resume,
            @RequestPart("jobDescription") String jobDescription)
            throws IOException {

        String resumeText = pdfService.extractText(resume);

        return aiService.reviewResume(resumeText, jobDescription);
    }
}
