package com.pavan.ai_resume_reviewer.repository;

import com.pavan.ai_resume_reviewer.entity.StudentProfile;
import com.pavan.ai_resume_reviewer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentProfileRepository
        extends JpaRepository<StudentProfile, Long> {

    Optional<StudentProfile> findByUser(User user);
}