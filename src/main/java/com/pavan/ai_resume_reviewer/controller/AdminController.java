package com.pavan.ai_resume_reviewer.controller;

import com.pavan.ai_resume_reviewer.dto.AdminDashboardResponse;
import com.pavan.ai_resume_reviewer.dto.ApiResponse;
import com.pavan.ai_resume_reviewer.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getDashboard() {
        AdminDashboardResponse response = adminService.getDashboardStats();
        return ResponseEntity.ok(new ApiResponse<>(true, "Admin dashboard stats retrieved successfully", response));
    }
}
