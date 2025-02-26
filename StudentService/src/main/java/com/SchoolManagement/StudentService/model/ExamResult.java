package com.SchoolManagement.StudentService.model;

import com.SchoolManagement.StudentService.dto.StudentPerformanceReport;
import com.SchoolManagement.StudentService.dto.Subject;
import com.SchoolManagement.StudentService.service.FeignConfigurationClient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "exam_results")
public class ExamResult {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private Long subjectId;

    private Double score;
    private String grade;
    private LocalDate examDate = LocalDate.now();

    @Transient
    private FeignConfigurationClient feignConfigurationClient;

    public ExamResult() {
        this.grade = grade;
    }

    @Autowired
    public void setScore(Double score) {
        this.score = score;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Double getScore() {
        return score;
    }


    public String getGrade() {
        return grade;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public StudentPerformanceReport.SubjectPerformance getSubject() {
        if (feignConfigurationClient == null) {
            return null;
        }
        Subject subject = feignConfigurationClient.getSubject(subjectId);
        if (subject == null) {
            return null;
        }

        StudentPerformanceReport.SubjectPerformance performance = new StudentPerformanceReport.SubjectPerformance();
        performance.setSubjectName(subject.getSubjectName());

        return performance;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
