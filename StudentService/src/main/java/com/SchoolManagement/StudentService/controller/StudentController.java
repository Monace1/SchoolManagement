package com.SchoolManagement.StudentService.controller;

import com.SchoolManagement.StudentService.dto.*;
import com.SchoolManagement.StudentService.model.ExamResult;
import com.SchoolManagement.StudentService.model.Student;
import com.SchoolManagement.StudentService.repository.StudentRepository;
import com.SchoolManagement.StudentService.service.ExamResultService;
import com.SchoolManagement.StudentService.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
    @PostMapping("/assign-subjects/{admissionNumber}")
    public ResponseEntity<Student> assignSubjectsToStudent(
            @PathVariable Integer admissionNumber,
            @RequestBody List<Long> subjectIds) {

        Student updatedStudent = studentService.assignSubjects(admissionNumber, subjectIds);
        return ResponseEntity.ok(updatedStudent);
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

    /*@GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/class/{classId}/stream/{streamId}")
    public ResponseEntity<List<Student>> getStudentsByClassAndStream(
            @PathVariable Long classId,
            @PathVariable Long streamId) {
        List<Student> students = studentService.getStudentsByClassAndStream(classId, streamId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/stream/{streamId}")
    public ResponseEntity<List<Student>> getStudentsByStream(@PathVariable Long streamId) {
        List<Student> students = studentService.getStudentsByStream(streamId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<Student>> getStudentsByClass(@PathVariable Long classId) {
        List<Student> students = studentService.getStudentsByClass(classId);
        return ResponseEntity.ok(students);
    }*/

    @GetMapping("/{admissionNumber}")
    public ResponseEntity<String> getStudentAdmissionNumber(@PathVariable Integer admissionNumber) {
        boolean exists = studentRepository.existsByAdmissionNumber(admissionNumber);

        if (!exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }

        return ResponseEntity.ok(String.valueOf(admissionNumber)); // Return admission number as String
    }


    @GetMapping("/{admissionNumber}/exists")
    public ResponseEntity<Boolean> checkStudentExists(@PathVariable Integer admissionNumber) {
        boolean exists = studentRepository.existsByAdmissionNumber(admissionNumber);
        return ResponseEntity.ok(exists);
    }


    /*@PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.updateFrom(updatedStudent);
                    Student savedStudent = studentRepository.save(student);
                    return ResponseEntity.ok(savedStudent);
                })
                .orElse(ResponseEntity.notFound().build());
    }*/

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

    @GetMapping("/{admissionNumber}/subjects")
    public ResponseEntity<List<Subject>> getStudentSubjects(@PathVariable Integer admissionNumber) {
        List<Subject> subjects = studentService.getStudentSubjects(admissionNumber);
        return ResponseEntity.ok(subjects);
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
    @PutMapping("/{admissionNumber}/set-password")
    public ResponseEntity<?> setStudentPassword(
            @PathVariable Integer admissionNumber,
            @RequestBody PasswordUpdateDto passwordUpdateDto) {

        Student student = studentRepository.findByAdmissionNumber(admissionNumber)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        if (!passwordEncoder.matches(passwordUpdateDto.getOldPassword(), student.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect current password");
        }

        student.setPassword(passwordEncoder.encode(passwordUpdateDto.getNewPassword()));
        studentRepository.save(student);

        return ResponseEntity.ok("Password updated successfully");
    }
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

    @GetMapping("/admission/{admissionNumber}/performance-report")
    public ResponseEntity<StudentPerformanceReport> getStudentPerformanceReportByAdmissionNumber(@PathVariable Integer admissionNumber) {
        return ResponseEntity.ok(studentService.generatePerformanceReportByAdmissionNumber(admissionNumber));
    }

   /* @GetMapping("/students/{studentId}/performance-report")
    public ResponseEntity<StudentPerformanceReport> getStudentPerformanceReport(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.generatePerformanceReport(studentId));
    }*/

}
