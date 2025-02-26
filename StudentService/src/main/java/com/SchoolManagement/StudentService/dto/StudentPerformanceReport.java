package com.SchoolManagement.StudentService.dto;

import com.SchoolManagement.StudentService.model.ExamResult;
import com.SchoolManagement.StudentService.model.Student;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class StudentPerformanceReport {
    private Student studentInfo;
    private Map<String, SubjectPerformance> subjectPerformances;
    private String overallGrade;

    public Student getStudentInfo() {
        return studentInfo;
    }
    public void setStudentInfo(Student studentInfo) {
        this.studentInfo = studentInfo;
    }
    public Map<String, SubjectPerformance> getSubjectPerformances() {
        return subjectPerformances;
    }
    public void setSubjectPerformances(Map<String, SubjectPerformance> subjectPerformances) {
        this.subjectPerformances = subjectPerformances;
    }
    public String getOverallGrade() {
        return overallGrade;
    }
    public void setOverallGrade(String overallGrade) {
        this.overallGrade = overallGrade;
    }

    @Data
    public static class SubjectPerformance {
        private String subjectName;
        private Double averageScore;
        private String grade;
        private List<ExamResult> examResults;

        public String getSubjectName() {
            return subjectName;
        }
        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }
        public Double getAverageScore() {
            return averageScore;
        }
        public void setAverageScore(Double averageScore) {
            this.averageScore = averageScore;
        }
        public String getGrade() {
            return grade;
        }
        public void setGrade(String grade) {
            this.grade = grade;
        }
        public List<ExamResult> getExamResults() {
            return examResults;
        }
        public void setExamResults(List<ExamResult> examResults) {
            this.examResults = examResults;
        }
        public void setTotalScore(double totalScore) {
        }
    }
}
