package com.SchoolManagement.ConfigurationService.repository;

import com.SchoolManagement.ConfigurationService.model.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StreamRepository extends JpaRepository<Stream,Long> {

    Optional<Stream> findByStreamName(String streamName);
}
