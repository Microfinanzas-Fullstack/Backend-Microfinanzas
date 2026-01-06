package com.silva.microfinanzas.infrastructure.persistence.jpa;

import com.silva.microfinanzas.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para User.
 */
@Repository
public interface JpaUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}

