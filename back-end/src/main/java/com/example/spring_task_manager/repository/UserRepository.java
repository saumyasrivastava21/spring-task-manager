package com.example.spring_task_manager.repository;

import com.example.spring_task_manager.entity.AssignedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AssignedUser, Long> {

    boolean existsByEmail(String email);
    Optional<AssignedUser> findByEmail(String email);
    @Query(value =
            """
            SELECT * FROM users
            WHERE :cursor IS NULL or id > :cursor
            ORDER BY id ASC
            LIMIT :sizeOfPage;
            """,
            nativeQuery = true
    )
    List<AssignedUser> fetchPage(@Param("cursor") Long cursor,
                                                @Param("sizeOfPage") Long sizeOfPage);
}
