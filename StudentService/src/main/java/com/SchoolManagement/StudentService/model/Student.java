package com.SchoolManagement.StudentService.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Integer admissionNumber;

    private String firstName;
    private String lastName;
    private LocalDate dateofbirth = LocalDate.now();

    @JoinColumn(name = "class_id")
    private Long schoolClass;

    @JoinColumn(name = "stream_id")
    private Long stream;

    @OneToMany(mappedBy = "student")
    private List<ExamResult> examResults;

    @Embedded
    private Address address;

    @ElementCollection
    private Map<String, ParentDetail> parentsDetails;

    public void updateFrom(Student updatedStudent) {

    }

    public Long getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(Long schoolClass) {
        this.schoolClass = schoolClass;
    }

    public Long getStream() {
        return stream;
    }

    public void setStream(Long stream) {
        this.stream = stream;
    }

    public List<ExamResult> getExamResults() {
        return examResults;
    }

    public void setExamResults(List<ExamResult> examResults) {
        this.examResults = examResults;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Map<String, ParentDetail> getParentsDetails() {
        return parentsDetails;
    }

    public void setParentsDetails(Map<String, ParentDetail> parentsDetails) {
        this.parentsDetails = parentsDetails;
    }

    public Integer getAdmissionNumber() {
        return admissionNumber;
    }

    public void setAdmissionNumber(Integer admissionNumber) {
        this.admissionNumber = admissionNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(LocalDate dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
    }
}

