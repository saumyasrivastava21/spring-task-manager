package com.example.spring_task_manager.repository;

import com.example.spring_task_manager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    boolean existsByTitle(String title);

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
}
