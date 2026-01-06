package com.silva.microfinanzas.domain.entities;

import com.silva.microfinanzas.domain.events.DomainEvent;
import com.silva.microfinanzas.domain.events.UserRegisteredEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entidad de dominio que representa un Usuario.
 *
 * User podría ser considerado un Aggregate Root en algunos contextos,
 * ya que gestiona su propio ciclo de vida y roles.
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "enabled")
    private Boolean enabled = true;

    /**
     * Roles del usuario.
     * ElementCollection para una colección simple de strings.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private User(String email, String password, String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.enabled = true;
        this.roles.add("ROLE_USER");
    }

    /**
     * Factory method para crear un nuevo usuario.
     */
    public static User create(String email, String password, String fullName) {
        validateEmail(email);
        validatePassword(password);
        validateFullName(fullName);

        User user = new User(email, password, fullName);

        // Registrar evento de dominio
        user.registerEvent(new UserRegisteredEvent(user.getId(), email, fullName));

        return user;
    }

    /**
     * Agrega un rol al usuario.
     */
    public void addRole(String role) {
        if (role != null && !role.trim().isEmpty()) {
            this.roles.add(role);
        }
    }

    /**
     * Deshabilita el usuario.
     */
    public void disable() {
        this.enabled = false;
    }

    /**
     * Habilita el usuario.
     */
    public void enable() {
        this.enabled = true;
    }

    /**
     * Actualiza la información del usuario.
     */
    public void updateInfo(String fullName) {
        validateFullName(fullName);
        this.fullName = fullName;
    }

    /**
     * Actualiza la contraseña del usuario.
     */
    public void updatePassword(String newPassword) {
        validatePassword(newPassword);
        this.password = newPassword;
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    private void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    // ============= Validaciones =============

    private static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
    }

    private static void validateFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
    }
}

