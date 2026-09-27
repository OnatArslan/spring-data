package com.onatarslan.springdata.todo;

import com.onatarslan.springdata.project.Project;
import com.onatarslan.springdata.project.ProjectNotFoundException;
import com.onatarslan.springdata.project.ProjectRepository;
import com.onatarslan.springdata.project.ProjectStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final TodoService todoService;
    private final ProjectRepository projectRepository;

    public TodoService(TodoRepository todoRepository, TodoService todoService, ProjectRepository projectRepository) {
        this.todoRepository = todoRepository;
        this.todoService = todoService;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public Todo create(UUID projectId, String title, Priority priority) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));

        boolean isProjectActive = project.getStatus().equals(ProjectStatus.ACTIVE);

        if (!isProjectActive) {
            throw new IllegalStateException();
        }

        Todo todo = new Todo(project, title, priority);

        return todoRepository.save(todo);
    }

    @Transactional
    public void moveTo(UUID todoId, UUID targetProjectId) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new TodoNotFoundException(todoId));

        Project project = projectRepository.getReferenceById(targetProjectId);

        todo.moveTo(project);
    }

}
