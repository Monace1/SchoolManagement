package com.SchoolManagement.StudentService.dto;

import java.util.List;

public class CreateExamResultDto {
    //private Long studentId;
    private Integer admissionNumber;
    private List<SubjectScore> subjectScores;
    /*public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }*/
    public List<SubjectScore> getSubjectScores() {
        return subjectScores;
    }

    public void setSubjectScores(List<SubjectScore> subjectScores) {
        this.subjectScores = subjectScores;
    }

    public Integer getAdmissionNumber() {
        return admissionNumber;
    }

    public void setAdmissionNumber(Integer admissionNumber) {
        this.admissionNumber = admissionNumber;
    }

    public static class SubjectScore {
        private Long subjectId;
        private Double score;

        public Long getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(Long subjectId) {
            this.subjectId = subjectId;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }

    }

}

