package com.silva.microfinanzas.domain.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Publisher de eventos de dominio.
 *
 * Esta clase actúa como un puente entre el dominio puro y la infraestructura de Spring.
 * Permite que las entidades de dominio publiquen eventos sin depender directamente
 * del framework Spring, manteniendo la pureza del modelo de dominio.
 */
@Slf4j
@Component
public class DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public DomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Publica un evento de dominio.
     *
     * @param event El evento a publicar
     */
    public void publish(DomainEvent event) {
        log.debug("Publishing domain event: {} with ID: {}",
                  event.getEventType(), event.getEventId());
        applicationEventPublisher.publishEvent(event);
    }
}

