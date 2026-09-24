package com.onatarslan.springdata.project;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.onatarslan.springdata.project.ProjectStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "projects")
public class Project {

    public static final int NAME_MAX_LENGTH = 120;

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Column(name = "name", nullable = false, length = NAME_MAX_LENGTH)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private ProjectStatus status;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Project() {
    }

    public Project(String name) {
        this.name = validName(name);
        this.status = ProjectStatus.ACTIVE;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void rename(String newName) {
        requireActive();
        String candidate = validName(newName);
        // Aynı değer: updatedAt'e dokunmayalım, yoksa gereksiz bir UPDATE ve version artışı olur
        if (candidate.equals(this.name)) {
            return;
        }
        this.name = candidate;
        touch();
    }

    public void archive() {
        if (status == ProjectStatus.ARCHIVED) {
            return;
        }
        this.status = ProjectStatus.ARCHIVED;
        touch();
    }

    private void requireActive() {
        if (status != ProjectStatus.ACTIVE) {
            throw new IllegalStateException("Archived project cannot be modified: " + id);
        }
    }

    private static String validName(String candidate) {
        Objects.requireNonNull(candidate, "name");
        String stripped = candidate.strip();
        if (stripped.isEmpty()) {
            throw new IllegalArgumentException("Project name must not be blank");
        }
        // PostgreSQL varchar(n) karakter (code point) sayar; String.length() UTF-16 birimi sayar
        if (stripped.codePointCount(0, stripped.length()) > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Project name must be at most %d characters".formatted(NAME_MAX_LENGTH));
        }
        return stripped;
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProjectStatus getStatus() {
        return status;
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