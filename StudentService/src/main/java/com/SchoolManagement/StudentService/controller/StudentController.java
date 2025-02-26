package com.SchoolManagement.StudentService.controller;

import com.SchoolManagement.StudentService.dto.*;
import com.SchoolManagement.StudentService.model.ExamResult;
import com.SchoolManagement.StudentService.model.Student;
import com.SchoolManagement.StudentService.repository.StudentRepository;
import com.SchoolManagement.StudentService.service.ExamResultService;
import com.SchoolManagement.StudentService.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    private final StudentService studentService;

    private final ExamResultService examResultService;

    public StudentController(StudentService studentService, ExamResultService examResultService) {
        this.studentService = studentService;
        this.examResultService = examResultService;
    }

    @PostMapping("/register")
    public ResponseEntity<Student> registerStudent(@Valid @RequestBody StudentRegistrationDto studentDto) {
        Student savedStudent = studentService.registerStudent(studentDto);
        return ResponseEntity.ok(savedStudent);
    }

    @PostMapping("/Examresults")
    public ResponseEntity<List<ExamResult>> saveExamResults(@RequestBody List<CreateExamResultDto> dtos) {
        List<ExamResult> savedResults = examResultService.saveExamResults(dtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedResults);
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return ResponseEntity.ok(students);
    }
    @GetMapping("/admission/{admissionNumber}")
    public ResponseEntity<Student> getStudentByAdmissionNumber(@PathVariable Integer admissionNumber) {
        return studentRepository.findByAdmissionNumber(admissionNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.updateFrom(updatedStudent);
                    Student savedStudent = studentRepository.save(student);
                    return ResponseEntity.ok(savedStudent);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/admissionNo/{admissionNo}")
    public ResponseEntity<Student> updateStudent(@PathVariable("admissionNo") Integer admissionNo, @RequestBody Student updatedStudent) {
        return studentRepository.findByAdmissionNumber(admissionNo)
                .map(student -> {
                    student.updateFrom(updatedStudent);
                    Student savedStudent = studentRepository.save(student);
                    return ResponseEntity.ok(savedStudent);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /*@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(student -> {
                    studentRepository.delete(student);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    */

    @GetMapping("/class/{classId}")
    public SchoolClass getClassDetails(@PathVariable Long classId) {
        return studentService.getClassDetails(classId);
    }
    @GetMapping("/stream/{streamId}")
    public Stream getStreamDetails(@PathVariable Long streamId) {
        return studentService.getStreamDetails(streamId);
    }
    @GetMapping("/subject/{subjectId}")
    public Subject getSubjectDetails(@PathVariable Long subjectId) {
        return studentService.getSubjectDetails(subjectId);
    }

    @GetMapping("/students/{studentId}/performance-report")
    public ResponseEntity<StudentPerformanceReport> getStudentPerformanceReport(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.generatePerformanceReport(studentId));
    }
}
