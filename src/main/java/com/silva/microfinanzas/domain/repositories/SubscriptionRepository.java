package com.silva.microfinanzas.domain.repositories;

import com.silva.microfinanzas.domain.entities.Subscription;
import com.silva.microfinanzas.domain.valueobjects.SubscriptionStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface del dominio para Subscription.
 */
public interface SubscriptionRepository {

    /**
     * Guarda una suscripción (crear o actualizar).
     */
    Subscription save(Subscription subscription);

    /**
     * Busca una suscripción por su ID.
     */
    Optional<Subscription> findById(Long id);

    /**
     * Busca todas las suscripciones de un usuario.
     */
    List<Subscription> findByUserId(Long userId);

    /**
     * Busca suscripciones de un usuario por estado.
     */
    List<Subscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status);

    /**
     * Busca suscripciones que deben ser cobradas en o antes de la fecha dada.
     */
    List<Subscription> findDueForBilling(LocalDate date);

    /**
     * Busca suscripciones activas de un usuario.
     */
    List<Subscription> findActiveByUserId(Long userId);

    /**
     * Elimina una suscripción.
     */
    void delete(Subscription subscription);

    /**
     * Elimina una suscripción por ID.
     */
    void deleteById(Long id);
}

