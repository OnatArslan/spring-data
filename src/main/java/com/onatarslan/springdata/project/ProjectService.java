package com.onatarslan.springdata.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProjectService {

    public static final Logger log = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    public UUID create(String name) {

        Project project = new Project(name);

        UUID id = projectRepository.save(project).getId();

        log.debug("Project persisted, id={}", id);

        return id;
    }

}
