package com.sancheck.backend.domain.memory.repository;

import com.sancheck.backend.domain.memory.entity.Memory;
import com.sancheck.backend.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryRepository extends JpaRepository<Memory, Long> {
    List<Memory> findByUser(User user);
}
