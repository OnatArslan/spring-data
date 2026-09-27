package com.onatarslan.springdata.project;

import com.onatarslan.springdata.todo.TodoStatus;
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
    private final ProjectSettingsRepository projectSettingsRepository;

    public ProjectService(ProjectRepository projectRepository, ProjectSettingsRepository projectSettingsRepository) {
        this.projectRepository = projectRepository;
        this.projectSettingsRepository = projectSettingsRepository;
    }

    @Transactional
    public Project create(String name) {
        Project project = projectRepository.save(new Project(name));
        projectSettingsRepository.save(new ProjectSettings(project, TodoStatus.TODO, 50));

        return project;
    }

}
