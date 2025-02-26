package com.SchoolManagement.ConfigurationService.service;

import com.SchoolManagement.ConfigurationService.dto.AssignStream;
import com.SchoolManagement.ConfigurationService.dto.CreateClassDto;
import com.SchoolManagement.ConfigurationService.dto.CreateStreamDto;
import com.SchoolManagement.ConfigurationService.exceptions.InvalidDataException;
import com.SchoolManagement.ConfigurationService.model.GradeSystem;
import com.SchoolManagement.ConfigurationService.model.SchoolClass;
import com.SchoolManagement.ConfigurationService.model.Stream;
import com.SchoolManagement.ConfigurationService.model.Subject;
import com.SchoolManagement.ConfigurationService.repository.SchoolClassRepository;
import com.SchoolManagement.ConfigurationService.repository.StreamRepository;
import com.SchoolManagement.ConfigurationService.repository.SubjectRepository;
import com.SchoolManagement.ConfigurationService.repository.GradeSystemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigService {

    private final SchoolClassRepository schoolClassRepository;
    private final StreamRepository streamRepository;
    private final SubjectRepository subjectRepository;
    private final GradeSystemRepository gradeSystemRepository;

    public ConfigService(SchoolClassRepository schoolClassRepository,
                         StreamRepository streamRepository,
                         SubjectRepository subjectRepository,
                         GradeSystemRepository gradeSystemRepository) {
        this.schoolClassRepository = schoolClassRepository;
        this.streamRepository = streamRepository;
        this.subjectRepository = subjectRepository;
        this.gradeSystemRepository = gradeSystemRepository;
    }
    public SchoolClass createClass(CreateClassDto createClassDto) throws InvalidDataException {
        validateSchoolClass(createClassDto);
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setClassName(createClassDto.getClassName());

        return schoolClassRepository.save(schoolClass);
    }


   /*public SchoolClass createClass(CreateClassDto classDto) throws InvalidDataException {
        validateSchoolClass(classDto);
        return schoolClassRepository.save(classDto);
    }*/

    public Optional<SchoolClass> getClassById(Long classId) {
        return schoolClassRepository.findById(classId);
    }

    public Stream assignStreamToClass(Long streamId, Long classId) {
        Stream stream = getStreamOrThrow(streamId);
        SchoolClass schoolClass = getSchoolClassOrThrow(classId);
        stream.setSchoolClass(schoolClass);
        return streamRepository.save(stream);
    }

    public Subject createSubject(Subject subject) throws InvalidDataException {
        validateSubject(subject);
        return subjectRepository.save(subject);
    }

    public Optional<Subject> getSubjectById(Long subjectId) {
        return subjectRepository.findById(subjectId);
    }
    public GradeSystem createGradeSystem(GradeSystem gradeSystem) throws InvalidDataException {
        validateGradeSystem(gradeSystem);
        return gradeSystemRepository.save(gradeSystem);
    }

    public String findGradeForScore(Double score) {
        return gradeSystemRepository.findAll()
                .stream()
                .filter(grade -> score >= grade.getMinScore() && score <= grade.getMaxScore())
                .map(GradeSystem::getGrade)
                .findFirst()
                .orElse("No grade available for this score");
    }

    private void validateSchoolClass(CreateClassDto schoolClass) throws InvalidDataException {
        if (schoolClass.getClassName() == null || schoolClass.getClassName().trim().isEmpty()) {
            throw new InvalidDataException("Class name cannot be empty");
        }

    }

    private void validateSubject(Subject subject) throws InvalidDataException {
        if (subject.getSubjectName() == null || subject.getSubjectName().trim().isEmpty()) {
            throw new InvalidDataException("Subject name cannot be empty");
        }
    }

    private void validateGradeSystem(GradeSystem gradeSystem) throws InvalidDataException {
        if (gradeSystem.getMinScore() == null || gradeSystem.getMaxScore() == null) {
            throw new InvalidDataException("Min and Max score must be provided");
        }
        if (gradeSystem.getMinScore() < 0 || gradeSystem.getMaxScore() > 100) {
            throw new InvalidDataException("Score range should be between 0 and 100");
        }
        if (gradeSystem.getGrade() == null || gradeSystem.getGrade().trim().isEmpty()) {
            throw new InvalidDataException("Grade cannot be empty");
        }
    }

    private SchoolClass getSchoolClassOrThrow(Long classId) {
        return schoolClassRepository.findById(classId)
                .orElseThrow(() -> new EntityNotFoundException("Class not found"));
    }

    private Stream getStreamOrThrow(Long streamId) {
        return streamRepository.findById(streamId)
                .orElseThrow(() -> new EntityNotFoundException("Stream not found"));
    }

    public Stream createStream(CreateStreamDto createStreamDto) {
        Optional<Stream> existingStream = streamRepository.findByStreamName(createStreamDto.getStreamName());
        if (existingStream.isPresent()) {
            throw new RuntimeException("Stream with name " + createStreamDto.getStreamName() + " already exists.");
        }
        Stream stream = new Stream();
        stream.setStreamName(createStreamDto.getStreamName());
        return streamRepository.save(stream);
    }


    public Object assignStreamToClass(AssignStream dto) {

        Optional<Stream> optionalStream = streamRepository.findById(dto.getStreamId());
        Optional<SchoolClass> optionalClass = schoolClassRepository.findById(dto.getClassId());

        if (optionalStream.isEmpty()) {
            throw new RuntimeException("Stream not found with ID: " + dto.getStreamId());
        }

        if (optionalClass.isEmpty()) {
            throw new RuntimeException("Class not found with ID: " + dto.getClassId());
        }

        Stream stream = optionalStream.get();
        stream.setSchoolClass(optionalClass.get());

        return streamRepository.save(stream);

    }

    public Optional<Stream> getStreamDetails(Long streamId) {
        return streamRepository.findById(streamId);
    }

    public List<SchoolClass> getAllClasses() {
        return schoolClassRepository.findAll();
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public List<Stream> getAllStreams() {
        return streamRepository.findAll();
    }
}
