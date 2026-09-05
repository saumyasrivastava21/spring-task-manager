package com.example.spring_task_manager.repository;

import com.example.spring_task_manager.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByName(String name);
    Optional<Project> findByName(String name);

    @Query(value =
            """
            SELECT * FROM project
            WHERE :cursor IS NULL or id > :cursor
            ORDER BY id ASC
            LIMIT :sizeOfPage;
            """,
            nativeQuery = true
    )
    List<Project> fetchData(@Param("cursor") Long cursor,
                                     @Param("sizeOfPage") Long sizeOfPage);
}
