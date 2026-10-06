package com.pavan.ai_resume_reviewer.dto;

public class RecruiterProfileResponse {

    private Long id;
    private String name;
    private String email;
    private String designation;
    private String phone;
    private String companyName;
    private Long companyId;

    public RecruiterProfileResponse(Long id, String name, String email, String designation, String phone, String companyName, Long companyId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.designation = designation;
        this.phone = phone;
        this.companyName = companyName;
        this.companyId = companyId;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getDesignation() { return designation; }
    public String getPhone() { return phone; }
    public String getCompanyName() { return companyName; }
    public Long getCompanyId() { return companyId; }
}
