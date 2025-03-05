package com.SchoolManagement.StudentService.repository;

import com.SchoolManagement.StudentService.model.ExamResult;
import com.SchoolManagement.StudentService.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExamResultRepository extends JpaRepository<ExamResult,Long> {
    List<ExamResult> findByStudentId(Long studentId);
    //Optional<Student> findByAdmissionNumber(Integer admissionNumber);

    //boolean existsByAdmissionNumber(Integer admissionNumber);

}
