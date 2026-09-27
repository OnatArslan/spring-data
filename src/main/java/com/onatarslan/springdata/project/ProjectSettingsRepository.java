package com.onatarslan.springdata.project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProjectSettingsRepository extends JpaRepository<ProjectSettings, UUID> {

    Optional<ProjectSettings> findByProjectId(UUID projectId);

}
