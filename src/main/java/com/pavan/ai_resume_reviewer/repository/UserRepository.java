package com.pavan.ai_resume_reviewer.repository;

import com.pavan.ai_resume_reviewer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}