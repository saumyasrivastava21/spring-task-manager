package com.example.spring_task_manager.repository;

import com.example.spring_task_manager.entity.Priority;
import com.example.spring_task_manager.entity.Status;
import com.example.spring_task_manager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    boolean existsByTitle(String title);
    List<Task> findByStatus(Status status);
    @Query(value =
            """
            SELECT *
            FROM task
            WHERE created_at < :date;
            """,
            nativeQuery = true
    )
    List<Task> findBeforeDate(@Param("date") LocalDateTime date);
    @Query(value =
            """
            SELECT * FROM task
            WHERE :cursor IS NULL or id > :cursor
            ORDER BY id ASC
            LIMIT :sizeOfPage;
            """,
            nativeQuery = true
    )
    List<Task> fetchPage(@Param("cursor") Long cursor,
                                @Param("sizeOfPage") Long sizeOfPage);
    @Query(value =
            """
            SELECT * FROM task t
            WHERE dead_line::date - CAST(:currentTime as date) = :days;
            """,
            nativeQuery = true)
    List<Task> fetchTasksNearTheirDeadline(@Param("days") Integer days, @Param("currentTime") LocalDateTime currentTime);

    @Query(value =
            """
            SELECT * FROM task
            WHERE status != :inactive
            ORDER BY dead_line;
            """,
            nativeQuery = true)
    List<Task> fetchActiveTasksOrderedByDeadline(@Param("inactive") String status);
    List<Task> findByAssignedUser_IdAndStatus(Long userId, Status status);
    List<Task> findByStatusAndPriority(Status status, Priority priority);
    @Query(value =
            """
            SELECT * FROM task
            WHERE assigned_user_id = :userId AND
                  dead_line::date BETWEEN CAST(:after as date) AND CAST(:before as date) AND
                  status = 'DONE';
            """
            ,nativeQuery = true)
    List<Task> fetchCompletedTasksByUserBetweenDates(
            @Param("userId") Long userId,
            @Param("after") LocalDateTime after,
            @Param("before") LocalDateTime before
    );
}
