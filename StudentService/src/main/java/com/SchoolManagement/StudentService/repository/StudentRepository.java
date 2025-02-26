package com.SchoolManagement.StudentService.repository;

import com.SchoolManagement.StudentService.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {
    boolean existsByAdmissionNumber(int admissionNumber);

    Optional<Student> findByAdmissionNumber(Integer admissionNumber);
}
