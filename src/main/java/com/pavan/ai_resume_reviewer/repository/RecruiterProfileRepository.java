package com.pavan.ai_resume_reviewer.repository;

import com.pavan.ai_resume_reviewer.entity.RecruiterProfile;
import com.pavan.ai_resume_reviewer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, Long> {
    Optional<RecruiterProfile> findByUser(User user);
}
