package com.SchoolManagement.StudentService.dto;

import java.util.List;

public class SchoolClass {
    private Long id;
    private String className;
    private List<Subject> subjects;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public List<Subject> getSubjects() { return subjects; }
    public void setSubjects(List<Subject> subjects) { this.subjects = subjects; }
}
