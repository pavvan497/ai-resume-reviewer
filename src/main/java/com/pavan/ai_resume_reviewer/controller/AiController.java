package com.pavan.ai_resume_reviewer.controller;

import com.pavan.ai_resume_reviewer.model.ResumeReview;
import com.pavan.ai_resume_reviewer.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.pavan.ai_resume_reviewer.model.ResumeReview;

@RestController
@RequestMapping("/ai")
public class AiController {


    private final  AiService aiService;

    public AiController(AiService aiService){
          this.aiService=aiService;
    }

    @GetMapping("/review")
    public ResumeReview review(@RequestParam String resume,
                               @RequestParam String jobDescription){
        return aiService.reviewResume(resume,jobDescription);
    }
}
