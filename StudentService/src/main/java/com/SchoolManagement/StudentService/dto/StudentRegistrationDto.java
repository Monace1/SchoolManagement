package com.SchoolManagement.StudentService.dto;

import com.SchoolManagement.StudentService.model.Address;
import com.SchoolManagement.StudentService.model.ParentDetail;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Map;

public class StudentRegistrationDto {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    private Integer admissionNumber;
    @Column(nullable = false)
    private String password;
    @NotBlank(message = "Date of birth is required")
    private LocalDate dateOfBirth;
    @NotNull(message = "Class ID is required")
    private Long schoolClass;
    @NotNull(message = "Stream ID is required")
    private Long stream;
    @NotNull(message = "Address is required")
    private Address address;
    @NotNull(message = "Parent details are required")
    @Size(min = 1, message = "At least one parent detail must be provided")
    private Map<String, ParentDetail> parentsDetails;

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }

    public void setDateOfBirth(@NotBlank LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Long getSchoolClass() { return schoolClass; }

    public void setSchoolClass(Long schoolClass) { this.schoolClass = schoolClass; }

    public Long getStream() { return stream; }

    public void setStream(Long stream) { this.stream = stream; }

    public Address getAddress() { return address; }

    public void setAddress(Address address) { this.address = address; }

    public Map<String, ParentDetail> getParentsDetails() { return parentsDetails; }

    public void setParentsDetails(Map<String, ParentDetail> parentsDetails) { this.parentsDetails = parentsDetails; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAdmissionNumber() {
        return admissionNumber;
    }

    public void setAdmissionNumber(Integer admissionNumber) {
        this.admissionNumber = admissionNumber;
    }
}

