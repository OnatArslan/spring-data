package com.onatarslan.springdata.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Optional<Project> findByName(String name);

    boolean existsByName(String name);

    List<Project> findByStatus(ProjectStatus status, Sort sort);

    long countByStatus(ProjectStatus status);
}
