package com.sancheck.backend.domain.user.repository;

import com.sancheck.backend.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBySocialId(String s);
    Optional<User> findById(Long i);
}
