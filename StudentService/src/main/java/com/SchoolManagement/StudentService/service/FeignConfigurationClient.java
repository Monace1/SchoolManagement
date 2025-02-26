package com.SchoolManagement.StudentService.service;

import com.SchoolManagement.StudentService.dto.SchoolClass;
import com.SchoolManagement.StudentService.dto.Stream;
import com.SchoolManagement.StudentService.dto.Subject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "CONFIGURATIONSERVICE")
public interface FeignConfigurationClient {

    @GetMapping("/api/config/grades/score/{score}")
    String findGradeForScore(@PathVariable("score") Double score);

    @GetMapping("/api/config/classes/{classId}")
    SchoolClass getClass(@PathVariable("classId") Long classId);

    @GetMapping("/api/config/streams/{streamId}")
    Stream getStream(@PathVariable("streamId") Long streamId);

    @GetMapping("/api/config/subjects/{subjectId}")
    Subject getSubject(@PathVariable("subjectId") Long subjectId);


}
