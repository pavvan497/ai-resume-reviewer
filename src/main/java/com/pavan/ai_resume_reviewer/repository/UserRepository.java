package com.pavan.ai_resume_reviewer.repository;

import com.pavan.ai_resume_reviewer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}