package com.silva.microfinanzas.domain.repositories;

import com.silva.microfinanzas.domain.entities.User;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface del dominio para User.
 */
public interface UserRepository {

    /**
     * Guarda un usuario (crear o actualizar).
     */
    User save(User user);

    /**
     * Busca un usuario por su ID.
     */
    Optional<User> findById(Long id);

    /**
     * Busca un usuario por email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca todos los usuarios.
     */
    List<User> findAll();

    /**
     * Verifica si existe un usuario con el email dado.
     */
    boolean existsByEmail(String email);

    /**
     * Elimina un usuario.
     */
    void delete(User user);

    /**
     * Elimina un usuario por ID.
     */
    void deleteById(Long id);
}

