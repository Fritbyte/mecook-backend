package com.mecook.mecookbackend.domain.repository;

import com.mecook.mecookbackend.domain.model.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    User save(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
