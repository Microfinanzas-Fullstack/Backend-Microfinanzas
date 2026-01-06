package com.silva.microfinanzas.domain.events;

import com.silva.microfinanzas.domain.valueobjects.Money;
import com.silva.microfinanzas.domain.valueobjects.TransactionType;
import lombok.Getter;

/**
 * Evento de dominio que se dispara cuando se crea una nueva transacción.
 *
 * Este evento puede ser escuchado por otros agregados o servicios
 * para realizar acciones como actualizar estadísticas, enviar notificaciones, etc.
 */
@Getter
public class TransactionCreatedEvent extends DomainEvent {

    private final Long transactionId;
    private final Long userId;
    private final Money amount;
    private final TransactionType type;
    private final String category;

    public TransactionCreatedEvent(Long transactionId, Long userId, Money amount,
                                   TransactionType type, String category) {
        super();
        this.transactionId = transactionId;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.category = category;
    }
}

