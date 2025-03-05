package com.SchoolManagement.StudentService.service;

import com.SchoolManagement.StudentService.dto.CreateExamResultDto;
import com.SchoolManagement.StudentService.dto.StudentPerformanceReport;
import com.SchoolManagement.StudentService.model.ExamResult;
import com.SchoolManagement.StudentService.model.Student;
import com.SchoolManagement.StudentService.repository.ExamResultRepository;
import com.SchoolManagement.StudentService.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExamResultService {

    @Autowired
    private ExamResultRepository examResultRepository;
    @Autowired
    private StudentRepository studentRepository;
    private final FeignConfigurationClient feignConfigurationClient;
    @Autowired
    public ExamResultService(FeignConfigurationClient feignConfigurationClient) {
        this.feignConfigurationClient = feignConfigurationClient;
    }

    public List<ExamResult> saveExamResults(List<CreateExamResultDto> dtos) {
        List<ExamResult> savedResults = new ArrayList<>();

        for (CreateExamResultDto dto : dtos) {
            Student student = studentRepository.findByAdmissionNumber(dto.getAdmissionNumber())
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            for (CreateExamResultDto.SubjectScore subjectScore : dto.getSubjectScores()) {
                ExamResult examResult = new ExamResult();
                examResult.setStudent(student);
                examResult.setSubjectId(subjectScore.getSubjectId());
                examResult.setScore(subjectScore.getScore());

                if (examResult.getScore() != null) {
                    try {
                        String grade = feignConfigurationClient.findGradeForScore(examResult.getScore());
                        System.out.println(" Debug: Grade fetched from FeignClient: " + grade);

                        if (grade == null) {
                            System.err.println("Warning: FeignClient returned null for score: " + examResult.getScore());
                        } else {
                            examResult.setGrade(grade);
                        }
                    } catch (Exception e) {
                        System.err.println("Error calling FeignClient: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                savedResults.add(examResultRepository.save(examResult));
                System.out.println(" ExamResult saved with grade: " + examResult.getGrade());
            }
        }
        return savedResults;
    }

    public StudentPerformanceReport generatePerformanceReport(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

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
                .collect(Collectors.groupingBy(
                        er -> feignConfigurationClient.getSubject(er.getSubjectId()).getSubjectName(),
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
                .mapToDouble(StudentPerformanceReport.SubjectPerformance::getAverageScore)
                .average()
                .orElse(0.0);

        return feignConfigurationClient.findGradeForScore(averageScore);
    }
}

