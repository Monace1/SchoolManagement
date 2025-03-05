package com.SchoolManagement.StudentService.service;

import com.SchoolManagement.StudentService.dto.*;
import com.SchoolManagement.StudentService.exception.InvalidDataException;
import com.SchoolManagement.StudentService.model.ExamResult;
import com.SchoolManagement.StudentService.model.Student;
import com.SchoolManagement.StudentService.repository.ExamResultRepository;
import com.SchoolManagement.StudentService.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service

public class StudentService {

    private final PasswordEncoder passwordEncoder;

    private final StudentRepository studentRepository;
    private final FeignConfigurationClient feignClient;

    @Autowired
    public StudentService(StudentRepository studentRepository, FeignConfigurationClient feignClient, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.feignClient = feignClient;
        this.passwordEncoder=passwordEncoder;
    }

    private final Random random = new Random();


    @Autowired
    private  ExamResultRepository examResultRepository;



    public SchoolClass getClassDetails(Long classId) {
        return feignClient.getClass(classId);
    }

    public Stream getStreamDetails(Long streamId) {
        return feignClient.getStream(streamId);
    }

    public Subject getSubjectDetails(Long subjectId) {
        return feignClient.getSubject(subjectId);
    }

    public Student registerStudent(StudentRegistrationDto dto) {
        Student student = new Student();

        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setPassword(passwordEncoder.encode(String.valueOf(dto.getAdmissionNumber())));
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setSchoolClass(dto.getSchoolClass());
        student.setStream(dto.getStream());
        student.setAddress(dto.getAddress());
        student.setParentsDetails(dto.getParentsDetails());

        int admissionNumber;
        do {
            admissionNumber = 100000 + random.nextInt(900000);
        } while (studentRepository.existsByAdmissionNumber(admissionNumber));

        student.setAdmissionNumber(admissionNumber);

        return studentRepository.save(student);
    }

    public Student assignSubjects(Integer admissionNumber, List<Long> subjectIds) {
        Student student = studentRepository.findByAdmissionNumber(admissionNumber)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        String subjectIdsStr = subjectIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        ResponseEntity<List<Subject>> response = feignClient.getSubjectsByIds(subjectIdsStr);

        if (response.getBody() == null || response.getBody().isEmpty()) {
            throw new RuntimeException("Subjects not found");
        }

        student.setSubjectIds(subjectIds);
        return studentRepository.save(student);
    }

    /*public List<Student> getStudentsByClassAndStream(Long classId, Long streamId) {
        return studentRepository.findBySchoolClass_IdAndStream_Id(classId, streamId);
    }

    public List<Student> getStudentsByStream(Long streamId) {
        return studentRepository.findByStream_Id(streamId);
    }

    public List<Student> getStudentsByClass(Long classId) {
        return studentRepository.findBySchoolClass_Id(classId);
    }
*/

    public List<Subject> getStudentSubjects(Integer admissionNumber) {
        Student student = studentRepository.findByAdmissionNumber(admissionNumber)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        String subjectIdsStr = student.getSubjectIds().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        ResponseEntity<List<Subject>> response = feignClient.getSubjectsByIds(subjectIdsStr);
        return response.getBody();
    }


    /* public Student createStudent(Student student) throws InvalidDataException {
        validateStudent(student);
        return studentRepository.save(student);
    }

    public Student updateStudent(Long studentId, Student updatedStudent) {
        Student existingStudent = getStudentOrThrow(studentId);
        existingStudent.updateFrom(updatedStudent);
        return studentRepository.save(existingStudent);
    }

    public void deleteStudent(Long studentId) {
        studentRepository.deleteById(studentId);
    }
*/
    /*public StudentPerformanceReport generatePerformanceReport(Long studentId) {
        Student student = getStudentOrThrow(studentId);
        List<ExamResult> examResults = examResultRepository.findByStudentId(studentId);

        StudentPerformanceReport report = new StudentPerformanceReport();
        report.setStudentInfo(student);

        Map<String, StudentPerformanceReport.SubjectPerformance> performanceBySubject =
                calculateSubjectWisePerformance(examResults);

        report.setSubjectPerformances(performanceBySubject);
        report.setOverallGrade(calculateOverallGrade(performanceBySubject));

        return report;
    }*/
    public StudentPerformanceReport generatePerformanceReportByAdmissionNumber(Integer admissionNumber) {
        Student student = studentRepository.findByAdmissionNumber(admissionNumber)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with admission number: " + admissionNumber));

        List<ExamResult> examResults = examResultRepository.findByStudentId(student.getId());

        StudentPerformanceReport report = new StudentPerformanceReport();
        report.setStudentInfo(student);

        Map<String, StudentPerformanceReport.SubjectPerformance> performanceBySubject =
                calculateSubjectWisePerformance(examResults);

        report.setSubjectPerformances(performanceBySubject);
        report.setOverallGrade(calculateOverallGrade(performanceBySubject));

        return report;
    }


    private Map<String, StudentPerformanceReport.SubjectPerformance> calculateSubjectWisePerformance(List<ExamResult> results) {
        return results.stream()
                .filter(er -> er.getSubject() != null)
                .collect(Collectors.groupingBy(
                        er -> er.getSubject().getSubjectName(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                this::calculateSubjectPerformance
                        )
                ));
    }

    private StudentPerformanceReport.SubjectPerformance calculateSubjectPerformance(List<ExamResult> examResults) {
        double totalScore = examResults.stream().mapToDouble(ExamResult::getScore).sum();
        double averageScore = totalScore / examResults.size();

        StudentPerformanceReport.SubjectPerformance subjectPerformance = new StudentPerformanceReport.SubjectPerformance();
        subjectPerformance.setAverageScore(averageScore);
        subjectPerformance.setTotalScore(totalScore);
        subjectPerformance.setExamResults(examResults);

        return subjectPerformance;
    }

    private String calculateOverallGrade(Map<String, StudentPerformanceReport.SubjectPerformance> performances) {
        double averageScore = performances.values().stream()
                .mapToDouble(sp -> sp.getAverageScore())
                .average()
                .orElse(0.0);

        return feignClient.findGradeForScore(averageScore);
    }

    private void validateStudent(Student student) throws InvalidDataException {
        if (student.getAdmissionNumber() <= 0) {
            throw new InvalidDataException("Admission number must be a positive number");
        }
        if (student.getFirstName() == null || student.getFirstName().trim().isEmpty()) {
            throw new InvalidDataException("First name cannot be empty");
        }
        if (student.getLastName() == null || student.getLastName().trim().isEmpty()) {
            throw new InvalidDataException("Last name cannot be empty");
        }
        if (student.getDateofbirth() == null) {
            throw new InvalidDataException("Date of birth is required");
        }
        if (student.getAddress() == null || student.getAddress().trim().isEmpty()) {
            throw new InvalidDataException("Address cannot be empty");
        }
    }

    private Student getStudentOrThrow(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
    }


}
