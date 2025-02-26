package com.SchoolManagement.StudentService.service;

import com.SchoolManagement.StudentService.dto.*;
import com.SchoolManagement.StudentService.exception.InvalidDataException;
import com.SchoolManagement.StudentService.model.ExamResult;
import com.SchoolManagement.StudentService.model.Student;
import com.SchoolManagement.StudentService.repository.ExamResultRepository;
import com.SchoolManagement.StudentService.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service

public class StudentService {

    private final Random random = new Random();

    @Autowired
    private  StudentRepository studentRepository;
    @Autowired
    private  ExamResultRepository examResultRepository;

    @Autowired
    private  FeignConfigurationClient feignConfigurationClient;

    public SchoolClass getClassDetails(Long classId) {
        return feignConfigurationClient.getClass(classId);
    }

    public Stream getStreamDetails(Long streamId) {
        return feignConfigurationClient.getStream(streamId);
    }

    public Subject getSubjectDetails(Long subjectId) {
        return feignConfigurationClient.getSubject(subjectId);
    }

    public Student registerStudent(StudentRegistrationDto dto) {
        Student student = new Student();

        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
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
    public StudentPerformanceReport generatePerformanceReport(Long studentId) {
        Student student = getStudentOrThrow(studentId);
        List<ExamResult> examResults = examResultRepository.findByStudentId(studentId);

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
                .filter(er -> er.getSubject() != null) // Filter out null subjects
                .collect(Collectors.groupingBy(
                        er -> er.getSubject().getSubjectName(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                this::calculateSubjectPerformance // Method to calculate SubjectPerformance
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

        return feignConfigurationClient.findGradeForScore(averageScore);
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
