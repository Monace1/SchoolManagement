package com.SchoolManagement.ConfigurationService.repository;

import com.SchoolManagement.ConfigurationService.model.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolClassRepository extends JpaRepository<SchoolClass,Long> {

    Optional<SchoolClass> findByClassName(String className);

}
