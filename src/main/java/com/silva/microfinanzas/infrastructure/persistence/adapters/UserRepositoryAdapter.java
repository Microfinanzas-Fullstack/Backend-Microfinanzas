package com.silva.microfinanzas.infrastructure.persistence.adapters;

import com.silva.microfinanzas.domain.entities.User;
import com.silva.microfinanzas.domain.repositories.UserRepository;
import com.silva.microfinanzas.infrastructure.persistence.jpa.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador para UserRepository.
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaRepository;

    @Override
    public User save(User user) {
        return jpaRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public void delete(User user) {
        jpaRepository.delete(user);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}

