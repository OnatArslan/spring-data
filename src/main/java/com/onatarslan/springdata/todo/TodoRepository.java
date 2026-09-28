package com.onatarslan.springdata.todo;

import com.onatarslan.springdata.project.ProjectStatus;
import org.hibernate.query.spi.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface TodoRepository extends JpaRepository<Todo, UUID> {
    List<Todo> findByStatus(TodoStatus status);

    List<Todo> findByProjectId(UUID projectId, Sort sort);

    List<Todo> findByProjectIdAndStatus(UUID projectId, TodoStatus status, Sort sort);

    List<Todo> findByProjectIdAndStatus(UUID projectId, TodoStatus status, Limit limit);

    Optional<Todo> findFirstByProjectIdOrderByCreatedAtDesc(UUID projectId);

    List<Todo> findTop10ByProjectIdAndStatusOrderByPriorityDescIdAsc(UUID projectId, TodoStatus status);

    long countByProjectIdAndStatus(UUID projectId, TodoStatus status);

    boolean existsByProjectIdAndStatus(UUID projectId, TodoStatus status);

    Stream<Todo> streamByStatus(TodoStatus status);

    @Query("""
        select t from Todo t
        where t.project.id = :projectId
          and t.status <> :done
          and (t.priority >= :minPriority or t.dueDate < :today)
        order by t.dueDate asc nulls last, t.id asc
        """)
    List<Todo> findNeedsAttention(UUID projectId, TodoStatus done, Priority minPriority, LocalDate today);

    @Query("""
        select t from Todo t
        join fetch t.project p
        where p.status = :projectStatus
          and t.status in :statuses
        order by p.name asc, t.id asc
        """)
    List<Todo> findOpenWithProject(ProjectStatus projectStatus, Collection<TodoStatus> statuses);

    @Query("""
        select distinct t from Todo t
        join t.todoTags tt
        join tt.tag g
        where t.project.id = :projectId
          and g.name in :tagNames
        """)
    List<Todo> findWithAnyTag(UUID projectId, Collection<String> tagNames);

    @Query("""
        select t from Todo t
        where t.project.id = :projectId
          and t.status in :statuses
        order by coalesce(t.dueDate, current_date) asc, t.id asc
        """)
    List<Todo> findOpenOrderedByUrgency(UUID projectId, Collection<TodoStatus> statuses);

    @Query("select t from Todo t where t.project.id = :projectId and t.status in :statuses")
    List<Todo> findOpen(UUID projectId, Collection<TodoStatus> statuses, Sort sort);


}
