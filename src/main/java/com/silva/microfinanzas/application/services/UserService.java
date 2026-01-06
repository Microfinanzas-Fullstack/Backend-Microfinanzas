package com.silva.microfinanzas.application.services;

import com.silva.microfinanzas.application.dtos.RegisterUserDTO;
import com.silva.microfinanzas.application.dtos.UserResponseDTO;
import com.silva.microfinanzas.application.mappers.UserMapper;
import com.silva.microfinanzas.domain.entities.User;
import com.silva.microfinanzas.domain.events.DomainEventPublisher;
import com.silva.microfinanzas.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicación para gestionar usuarios.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final DomainEventPublisher eventPublisher;

    /**
     * Registra un nuevo usuario.
     */
    public UserResponseDTO registerUser(RegisterUserDTO dto) {
        log.info("Registering new user with email: {}", dto.getEmail());

        // Verificar que el email no esté en uso
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        // Encriptar la contraseña
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // Crear el usuario usando el factory method del dominio
        User user = User.create(dto.getEmail(), encodedPassword, dto.getFullName());

        // Persistir el usuario
        User savedUser = userRepository.save(user);

        // Publicar eventos de dominio
        savedUser.getDomainEvents().forEach(eventPublisher::publish);
        savedUser.clearDomainEvents();

        log.info("User registered successfully with ID: {}", savedUser.getId());

        return userMapper.toResponseDTO(savedUser);
    }

    /**
     * Obtiene un usuario por ID.
     */
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long userId) {
        log.debug("Getting user by ID: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toResponseDTO(user);
    }

    /**
     * Obtiene un usuario por email.
     */
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {
        log.debug("Getting user by email: {}", email);

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toResponseDTO(user);
    }

    /**
     * Actualiza la información de un usuario.
     */
    public UserResponseDTO updateUser(Long userId, String fullName) {
        log.info("Updating user: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.updateInfo(fullName);

        User updatedUser = userRepository.save(user);

        log.info("User updated successfully");

        return userMapper.toResponseDTO(updatedUser);
    }

    /**
     * Deshabilita un usuario.
     */
    public void disableUser(Long userId) {
        log.info("Disabling user: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.disable();
        userRepository.save(user);

        log.info("User disabled successfully");
    }

    /**
     * Habilita un usuario.
     */
    public void enableUser(Long userId) {
        log.info("Enabling user: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.enable();
        userRepository.save(user);

        log.info("User enabled successfully");
    }
}

