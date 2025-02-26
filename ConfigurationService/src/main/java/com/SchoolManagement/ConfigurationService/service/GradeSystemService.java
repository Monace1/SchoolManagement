package com.SchoolManagement.ConfigurationService.service;

import com.SchoolManagement.ConfigurationService.exceptions.InvalidDataException;
import com.SchoolManagement.ConfigurationService.model.GradeSystem;
import com.SchoolManagement.ConfigurationService.repository.GradeSystemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GradeSystemService {

    private final GradeSystemRepository gradeSystemRepository;

    public GradeSystemService(GradeSystemRepository gradeSystemRepository) {
        this.gradeSystemRepository = gradeSystemRepository;
    }

    public GradeSystem save(GradeSystem gradeSystem) throws InvalidDataException {
        validateGradeSystem(gradeSystem);
        return gradeSystemRepository.save(gradeSystem);
    }

    public Optional<GradeSystem> findById(Long id) {
        return gradeSystemRepository.findById(id);
    }

    public List<GradeSystem> findAll() {
        return gradeSystemRepository.findAll();
    }

    public GradeSystem update(GradeSystem gradeSystem) throws InvalidDataException {
        validateGradeSystem(gradeSystem);
        return gradeSystemRepository.save(gradeSystem);
    }

    public void delete(Long id) {
        gradeSystemRepository.deleteById(id);
    }

    public GradeSystem findGradeForScore(Double score) {
        return (GradeSystem) gradeSystemRepository.findByMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(score, score)
                .orElseThrow(() -> new EntityNotFoundException("No grade found for score: " + score));
    }

    private void validateGradeSystem(GradeSystem gradeSystem) throws InvalidDataException {
        if (gradeSystem.getMinScore() == null || gradeSystem.getMaxScore() == null) {
            throw new InvalidDataException("Min and max scores are required");
        }
        if (gradeSystem.getMinScore() >= gradeSystem.getMaxScore()) {
            throw new InvalidDataException("Min score must be less than max score");
        }
        if (gradeSystem.getGrade() == null || gradeSystem.getGrade().trim().isEmpty()) {
            throw new InvalidDataException("Grade cannot be empty");
        }
        if (gradeSystem.getPoints() == null || gradeSystem.getPoints() < 0) {
            throw new InvalidDataException("Points must be non-negative");
        }
    }
}
