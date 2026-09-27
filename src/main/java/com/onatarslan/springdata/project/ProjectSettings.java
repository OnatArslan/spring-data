package com.onatarslan.springdata.project;

import com.onatarslan.springdata.todo.TodoStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "project_settings")
public class ProjectSettings {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    // FK bu tabloda: owning side. LAZY burada gerçekten çalışır.
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false, unique = true)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_todo_status", nullable = false, length = 32)
    private TodoStatus defaultTodoStatus;

    @Column(name = "max_open_todos", nullable = false)
    private int maxOpenTodos;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected ProjectSettings() {
    }

    public ProjectSettings(Project project, TodoStatus defaultTodoStatus, int maxOpenTodos) {
        this.project = Objects.requireNonNull(project, "project");
        this.defaultTodoStatus = Objects.requireNonNull(defaultTodoStatus, "defaultTodoStatus");
        if (maxOpenTodos <= 0) {
            throw new IllegalArgumentException("maxOpenTodos must be positive");
        }
        this.maxOpenTodos = maxOpenTodos;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public TodoStatus getDefaultTodoStatus() {
        return defaultTodoStatus;
    }

    public int getMaxOpenTodos() {
        return maxOpenTodos;
    }
}