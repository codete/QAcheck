package com.codete.regression.authentication.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByApiKey(String apiKey);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByApiKey(String apiKey);
}
