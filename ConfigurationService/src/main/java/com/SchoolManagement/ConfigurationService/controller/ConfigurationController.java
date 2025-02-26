package com.SchoolManagement.ConfigurationService.controller;

import com.SchoolManagement.ConfigurationService.dto.AssignStream;
import com.SchoolManagement.ConfigurationService.dto.CreateClassDto;
import com.SchoolManagement.ConfigurationService.dto.CreateStreamDto;
import com.SchoolManagement.ConfigurationService.model.GradeSystem;
import com.SchoolManagement.ConfigurationService.model.SchoolClass;
import com.SchoolManagement.ConfigurationService.model.Stream;
import com.SchoolManagement.ConfigurationService.model.Subject;
import com.SchoolManagement.ConfigurationService.service.ConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api/config")
public class ConfigurationController {

    private final ConfigService configService;

    public ConfigurationController(ConfigService configService) {
        this.configService = configService;
    }



    @PostMapping("/classes")
    public ResponseEntity<SchoolClass> createClass(@RequestBody CreateClassDto createClassDto) {
        return ResponseEntity.ok(configService.createClass(createClassDto));
    }

    @GetMapping("/classes/{classId}")
    public ResponseEntity<Optional<SchoolClass>> getClassById(@PathVariable Long classId) {
        return ResponseEntity.ok(configService.getClassById(classId));
    }

    @PostMapping("/streams")
    public ResponseEntity<Stream> createStream(@RequestBody CreateStreamDto createStreamDto) {
        return ResponseEntity.ok(configService.createStream(createStreamDto));
    }

    @PutMapping("/streams/assign")
    public ResponseEntity<Stream> assignStreamToClass(@RequestBody AssignStream dto) {
        return ResponseEntity.ok((Stream) configService.assignStreamToClass(dto));
    }


  /*  @PutMapping("/streams/{streamId}/class/{classId}")
    public ResponseEntity<Stream> assignStreamToClass(
            @PathVariable Long streamId,
            @PathVariable Long classId) {
        return ResponseEntity.ok(configService.assignStreamToClass(streamId, classId));
    }
*/

    @PostMapping("/subjects")
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject) {
        return ResponseEntity.ok(configService.createSubject(subject));
    }
    @GetMapping("/subjects/{subjectId}")
    public ResponseEntity<Optional<Subject>> getSubjectById(@PathVariable Long subjectId) {
        return ResponseEntity.ok(configService.getSubjectById(subjectId));
    }
    @PostMapping("/grades")
    public ResponseEntity<GradeSystem> createGradeSystem(@RequestBody GradeSystem gradeSystem) {
        return ResponseEntity.ok(configService.createGradeSystem(gradeSystem));
    }

    @GetMapping("/stream/{streamId}")
    public Optional<Stream> getStreamDetails(@PathVariable Long streamId) {
        return configService.getStreamDetails(streamId);
    }

    @GetMapping("/grades/score/{score}")
    public ResponseEntity<String> findGradeForScore(@PathVariable Double score) {
        return ResponseEntity.ok(configService.findGradeForScore(score));
    }
    @GetMapping("/class")
    public ResponseEntity<List<SchoolClass>> getAllClasses() {
        List<SchoolClass> classes = configService.getAllClasses();
        return ResponseEntity.ok(classes);
    }
    @GetMapping("/streams")
    public ResponseEntity<List<Stream>> getAllStreams() {
        List<Stream> streams = configService.getAllStreams();
        return ResponseEntity.ok(streams);
    }
    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        List<Subject> subjects = configService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }
}
