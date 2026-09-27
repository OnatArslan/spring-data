package com.onatarslan.springdata.todo;


import com.onatarslan.springdata.tag.Tag;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "todo_tags")
public class TodoTag {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Column(name = "added_at", nullable = false)
    private Instant addedAt;

    protected TodoTag() {

    }

    // package-private: bağlantıyı yalnız Todo oluşturur
    TodoTag(Todo todo, Tag tag) {
        this.todo = Objects.requireNonNull(todo, "todo");
        this.tag = Objects.requireNonNull(tag, "tag");
        this.addedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public Todo getTodo() {
        return todo;
    }

    public Tag getTag() {
        return tag;
    }

    public Instant getAddedAt() {
        return addedAt;
    }


}
