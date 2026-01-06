package com.silva.microfinanzas.domain.events;

import lombok.Getter;

/**
 * Evento de dominio que se dispara cuando un usuario se registra en el sistema.
 */
@Getter
public class UserRegisteredEvent extends DomainEvent {

    private final Long userId;
    private final String email;
    private final String fullName;

    public UserRegisteredEvent(Long userId, String email, String fullName) {
        super();
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
    }
}

