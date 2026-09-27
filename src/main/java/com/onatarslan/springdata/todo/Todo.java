package com.onatarslan.springdata.todo;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.onatarslan.springdata.project.Project;
import com.onatarslan.springdata.todo.PriorityConverter;
import jakarta.persistence.*;

import org.hibernate.annotations.UuidGenerator;

// project_id §5'te map edildiğinde public constructor gelecek; o zamana kadar persist edilemez.
@Entity
@Table(name = "todos")
public class Todo {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private TodoStatus status;

    @Convert(converter = PriorityConverter.class)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Todo() {
    }

    public Todo(Project project, String title, Priority priority) {
        this.project = Objects.requireNonNull(project, "project");
        this.title = validTitle(title);
        this.priority = Objects.requireNonNull(priority, "priority");
        this.status = TodoStatus.TODO;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void moveTo(Project target) {
        Objects.requireNonNull(target, "target");
        requireNotDone();
        // Aynı project mi? id karşılaştırması proxy'yi initialize etmez
        if (target.getId().equals(project.getId())) {
            return;
        }
        this.project = target;
        touch();
    }

    private static String validTitle(String candidate) {
        Objects.requireNonNull(candidate, "title");
        String stripped = candidate.strip();
        if (stripped.isEmpty() || stripped.length() > 200) {
            throw new IllegalArgumentException("Title must be 1-200 characters");
        }
        return stripped;
    }

    public Project getProject() {
        return project;
    }

    public void start() {
        if (status != TodoStatus.TODO) {
            throw new IllegalStateException("Only TODO items can be started, current: " + status);
        }
        this.status = TodoStatus.IN_PROGRESS;
        touch();
    }

    public void complete() {
        if (status == TodoStatus.DONE) {
            throw new IllegalStateException("Todo is already done: " + id);
        }
        this.status = TodoStatus.DONE;
        touch();
    }

    public void changePriority(Priority newPriority) {
        Objects.requireNonNull(newPriority, "priority");
        requireNotDone();
        if (newPriority.equals(this.priority)) {
            return;
        }
        this.priority = newPriority;
        touch();
    }

    // null: son tarihi kaldır
    public void reschedule(LocalDate newDueDate) {
        requireNotDone();
        if (Objects.equals(newDueDate, this.dueDate)) {
            return;
        }
        this.dueDate = newDueDate;
        touch();
    }

    private void requireNotDone() {
        if (status == TodoStatus.DONE) {
            throw new IllegalStateException("Done todo cannot be modified: " + id);
        }
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public TodoStatus getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public Optional<LocalDate> getDueDate() {
        return Optional.ofNullable(dueDate);
    }

    public Long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}