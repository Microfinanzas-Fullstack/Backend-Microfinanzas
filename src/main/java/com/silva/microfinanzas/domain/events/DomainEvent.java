package com.silva.microfinanzas.domain.events;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Clase base abstracta para todos los eventos de dominio.
 *
 * En DDD, los Domain Events representan algo que ha ocurrido en el dominio
 * y que es relevante para el negocio. Son inmutables y capturan el estado
 * en el momento en que ocurrió el evento.
 *
 * Los eventos permiten la comunicación entre agregados sin acoplarlos directamente.
 */
@Getter
public abstract class DomainEvent {

    /**
     * Identificador único del evento.
     */
    private final String eventId;

    /**
     * Timestamp de cuándo ocurrió el evento.
     */
    private final LocalDateTime occurredOn;

    protected DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = LocalDateTime.now();
    }

    /**
     * Nombre del tipo de evento. Útil para serialización y logging.
     */
    public String getEventType() {
        return this.getClass().getSimpleName();
    }
}

