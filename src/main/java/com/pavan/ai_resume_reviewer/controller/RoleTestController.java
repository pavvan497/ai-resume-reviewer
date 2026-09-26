package com.pavan.ai_resume_reviewer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class RoleTestController {

    @GetMapping("/student/test")
    public String studentTest() {
        return "Welcome Student";
    }

    @GetMapping("/recruiter/test")
    public String recruiterTest() {
        return "Welcome Recruiter";
    }

    @GetMapping("/admin/test")
    public String adminTest() {
        return "Welcome Admin";
    }
}