package com.silva.microfinanzas.domain.events;

import com.silva.microfinanzas.domain.valueobjects.SubscriptionStatus;
import lombok.Getter;

/**
 * Evento de dominio que se dispara cuando cambia el estado de una suscripción.
 */
@Getter
public class SubscriptionStatusChangedEvent extends DomainEvent {

    private final Long subscriptionId;
    private final Long userId;
    private final SubscriptionStatus previousStatus;
    private final SubscriptionStatus newStatus;

    public SubscriptionStatusChangedEvent(Long subscriptionId, Long userId,
                                         SubscriptionStatus previousStatus,
                                         SubscriptionStatus newStatus) {
        super();
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
    }
}

