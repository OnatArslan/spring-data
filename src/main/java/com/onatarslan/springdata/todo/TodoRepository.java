package com.onatarslan.springdata.todo;

import org.hibernate.query.spi.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodoRepository extends JpaRepository<Todo, UUID> {
    List<Todo> findByStatus(TodoStatus status);

    long countByProjectIdAndStatus(UUID projectId, TodoStatus status);

    boolean existsByProjectIdAndStatus(UUID projectId, TodoStatus status);

    Optional<Todo> findFirstByProjectIdOrderByCreatedAtDesc(UUID projectId);

    List<Todo> findTop10ByStatusOrderByPriorityDesc(TodoStatus status);

    List<Todo> findByProjectId(UUID projectId, Sort sort);

    List<Todo> findByProjectId(UUID projectId, Limit limit);
}
