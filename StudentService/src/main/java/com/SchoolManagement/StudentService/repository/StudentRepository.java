package com.SchoolManagement.StudentService.repository;

import com.SchoolManagement.StudentService.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {
    

    Optional<Student> findByAdmissionNumber(Integer admissionNumber);
    boolean existsByAdmissionNumber(Integer admissionNumber);

    // List<Student> findBySchoolClass_IdAndStream_Id(Long classId, Long streamId);
  //  List<Student> findByStream_Id(Long streamId);
    // List<Student> findBySchoolClass_Id(Long classId);

}
