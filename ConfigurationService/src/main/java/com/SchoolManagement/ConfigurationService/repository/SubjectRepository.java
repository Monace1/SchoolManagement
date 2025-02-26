package com.SchoolManagement.ConfigurationService.repository;

import com.SchoolManagement.ConfigurationService.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject,Long> {
}
