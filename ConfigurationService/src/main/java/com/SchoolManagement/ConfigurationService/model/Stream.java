package com.SchoolManagement.ConfigurationService.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "streams")
public class Stream {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String streamName;
    @ManyToOne
    @JoinColumn(name = "class_id")
    private SchoolClass schoolClass;

    public void setSchoolClass(SchoolClass schoolClass) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }
}

