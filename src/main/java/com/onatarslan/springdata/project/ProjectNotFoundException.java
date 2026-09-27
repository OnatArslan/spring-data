package com.onatarslan.springdata.project;

import java.util.UUID;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(UUID projectId) {
        super("Project not found with id: " + projectId);
    }
}
