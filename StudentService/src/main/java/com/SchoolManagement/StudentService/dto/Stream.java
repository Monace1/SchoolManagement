package com.SchoolManagement.StudentService.dto;

public class Stream {
    private Long id;
    private String streamName;
    private SchoolClass schoolClass;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStreamName() { return streamName; }
    public void setStreamName(String streamName) { this.streamName = streamName; }
    public SchoolClass getSchoolClass() { return schoolClass; }
    public void setSchoolClass(SchoolClass schoolClass) { this.schoolClass = schoolClass; }
}
