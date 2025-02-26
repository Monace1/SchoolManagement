package com.SchoolManagement.ConfigurationService.repository;

import com.SchoolManagement.ConfigurationService.model.GradeSystem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GradeSystemRepository extends JpaRepository<GradeSystem,Long> {
    Optional<Object> findByMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(Double score, Double score1);
}
