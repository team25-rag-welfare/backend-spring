package com.sancheck.backend.domain.memory.repository;

import com.sancheck.backend.domain.memory.entity.Memory;
import com.sancheck.backend.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemoryRepository extends JpaRepository<Memory, Long> {
    List<Memory> findByUser(User user);

    @Modifying
    @Query("UPDATE Memory m SET m.deletedAt = CURRENT_TIMESTAMP WHERE m.user = :user AND m.deletedAt IS NULL")
    void softDeleteAllByUser(@Param("user") User user);
}
