package com.pavan.ai_resume_reviewer.repository;

import com.pavan.ai_resume_reviewer.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByName(String name);
}
