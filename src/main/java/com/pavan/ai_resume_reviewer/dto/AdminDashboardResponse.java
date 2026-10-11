package com.pavan.ai_resume_reviewer.dto;

import java.util.List;

public class AdminDashboardResponse {

    private long totalStudents;
    private long totalRecruiters;
    private long totalCompanies;
    private long totalActiveJobs;
    private long totalApplications;
    private List<JobApplicationResponse> recentApplications;

    public AdminDashboardResponse(long totalStudents, long totalRecruiters, long totalCompanies,
                                  long totalActiveJobs, long totalApplications,
                                  List<JobApplicationResponse> recentApplications) {
        this.totalStudents = totalStudents;
        this.totalRecruiters = totalRecruiters;
        this.totalCompanies = totalCompanies;
        this.totalActiveJobs = totalActiveJobs;
        this.totalApplications = totalApplications;
        this.recentApplications = recentApplications;
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getTotalRecruiters() {
        return totalRecruiters;
    }

    public void setTotalRecruiters(long totalRecruiters) {
        this.totalRecruiters = totalRecruiters;
    }

    public long getTotalCompanies() {
        return totalCompanies;
    }

    public void setTotalCompanies(long totalCompanies) {
        this.totalCompanies = totalCompanies;
    }

    public long getTotalActiveJobs() {
        return totalActiveJobs;
    }

    public void setTotalActiveJobs(long totalActiveJobs) {
        this.totalActiveJobs = totalActiveJobs;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public List<JobApplicationResponse> getRecentApplications() {
        return recentApplications;
    }

    public void setRecentApplications(List<JobApplicationResponse> recentApplications) {
        this.recentApplications = recentApplications;
    }
}
