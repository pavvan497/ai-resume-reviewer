package com.pavan.ai_resume_reviewer.repository;

import com.pavan.ai_resume_reviewer.entity.JobApplication;
import com.pavan.ai_resume_reviewer.entity.JobDrive;
import com.pavan.ai_resume_reviewer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByStudent(User student);
    List<JobApplication> findByJobDrive(JobDrive jobDrive);
    Optional<JobApplication> findByStudentAndJobDrive(User student, JobDrive jobDrive);
}
