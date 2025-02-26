package com.SchoolManagement.StudentService.repository;

import com.SchoolManagement.StudentService.model.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamResultRepository extends JpaRepository<ExamResult,Long> {
    List<ExamResult> findByStudentId(Long studentId);
}
