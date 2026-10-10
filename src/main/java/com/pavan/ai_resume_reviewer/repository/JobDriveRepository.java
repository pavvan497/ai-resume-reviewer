package com.pavan.ai_resume_reviewer.repository;

import com.pavan.ai_resume_reviewer.entity.JobDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobDriveRepository extends JpaRepository<JobDrive, Long> {
    List<JobDrive> findByIsActiveTrue();
}
