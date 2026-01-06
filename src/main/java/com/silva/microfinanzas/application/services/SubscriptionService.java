package com.silva.microfinanzas.application.services;

import com.silva.microfinanzas.application.dtos.CreateSubscriptionDTO;
import com.silva.microfinanzas.application.dtos.SubscriptionResponseDTO;
import com.silva.microfinanzas.application.mappers.SubscriptionMapper;
import com.silva.microfinanzas.domain.entities.Subscription;
import com.silva.microfinanzas.domain.events.DomainEventPublisher;
import com.silva.microfinanzas.domain.repositories.SubscriptionRepository;
import com.silva.microfinanzas.domain.valueobjects.Money;
import com.silva.microfinanzas.domain.valueobjects.SubscriptionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de aplicación para gestionar suscripciones.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final DomainEventPublisher eventPublisher;

    /**
     * Crea una nueva suscripción.
     */
    public SubscriptionResponseDTO createSubscription(Long userId, CreateSubscriptionDTO dto) {
        log.info("Creating subscription for user: {}", userId);

        Money amount = Money.of(dto.getAmount(), dto.getCurrency());

        Subscription subscription = Subscription.create(
            userId,
            dto.getName(),
            dto.getDescription(),
            amount,
            dto.getBillingCycleDays(),
            dto.getNextBillingDate()
        );

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        log.info("Subscription created successfully with ID: {}", savedSubscription.getId());

        return subscriptionMapper.toResponseDTO(savedSubscription);
    }

    /**
     * Obtiene una suscripción por ID.
     */
    @Transactional(readOnly = true)
    public SubscriptionResponseDTO getSubscriptionById(Long userId, Long subscriptionId) {
        log.debug("Getting subscription {} for user {}", subscriptionId, userId);

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
            .orElseThrow(() -> new RuntimeException("Subscription not found"));

        if (!subscription.getUserId().equals(userId)) {
            throw new RuntimeException("Subscription does not belong to user");
        }

        return subscriptionMapper.toResponseDTO(subscription);
    }

    /**
     * Obtiene todas las suscripciones de un usuario.
     */
    @Transactional(readOnly = true)
    public List<SubscriptionResponseDTO> getUserSubscriptions(Long userId) {
        log.debug("Getting all subscriptions for user {}", userId);

        return subscriptionRepository.findByUserId(userId).stream()
            .map(subscriptionMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene las suscripciones activas de un usuario.
     */
    @Transactional(readOnly = true)
    public List<SubscriptionResponseDTO> getActiveSubscriptions(Long userId) {
        log.debug("Getting active subscriptions for user {}", userId);

        return subscriptionRepository.findActiveByUserId(userId).stream()
            .map(subscriptionMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Pausa una suscripción.
     */
    public SubscriptionResponseDTO pauseSubscription(Long userId, Long subscriptionId) {
        log.info("Pausing subscription {} for user {}", subscriptionId, userId);

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
            .orElseThrow(() -> new RuntimeException("Subscription not found"));

        if (!subscription.getUserId().equals(userId)) {
            throw new RuntimeException("Subscription does not belong to user");
        }

        subscription.pause();

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        // Publicar eventos de dominio
        savedSubscription.getDomainEvents().forEach(eventPublisher::publish);
        savedSubscription.clearDomainEvents();

        log.info("Subscription paused successfully");

        return subscriptionMapper.toResponseDTO(savedSubscription);
    }

    /**
     * Cancela una suscripción.
     */
    public SubscriptionResponseDTO cancelSubscription(Long userId, Long subscriptionId) {
        log.info("Cancelling subscription {} for user {}", subscriptionId, userId);

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
            .orElseThrow(() -> new RuntimeException("Subscription not found"));

        if (!subscription.getUserId().equals(userId)) {
            throw new RuntimeException("Subscription does not belong to user");
        }

        subscription.cancel();

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        // Publicar eventos de dominio
        savedSubscription.getDomainEvents().forEach(eventPublisher::publish);
        savedSubscription.clearDomainEvents();

        log.info("Subscription cancelled successfully");

        return subscriptionMapper.toResponseDTO(savedSubscription);
    }

    /**
     * Reactiva una suscripción.
     */
    public SubscriptionResponseDTO reactivateSubscription(Long userId, Long subscriptionId) {
        log.info("Reactivating subscription {} for user {}", subscriptionId, userId);

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
            .orElseThrow(() -> new RuntimeException("Subscription not found"));

        if (!subscription.getUserId().equals(userId)) {
            throw new RuntimeException("Subscription does not belong to user");
        }

        subscription.reactivate();

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        // Publicar eventos de dominio
        savedSubscription.getDomainEvents().forEach(eventPublisher::publish);
        savedSubscription.clearDomainEvents();

        log.info("Subscription reactivated successfully");

        return subscriptionMapper.toResponseDTO(savedSubscription);
    }

    /**
     * Elimina una suscripción.
     */
    public void deleteSubscription(Long userId, Long subscriptionId) {
        log.info("Deleting subscription {} for user {}", subscriptionId, userId);

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
            .orElseThrow(() -> new RuntimeException("Subscription not found"));

        if (!subscription.getUserId().equals(userId)) {
            throw new RuntimeException("Subscription does not belong to user");
        }

        subscriptionRepository.delete(subscription);

        log.info("Subscription deleted successfully");
    }

    /**
     * Procesa suscripciones que deben ser cobradas.
     * Este método podría ser llamado por un scheduled task.
     */
    public void processDueSubscriptions() {
        log.info("Processing due subscriptions");

        List<Subscription> dueSubscriptions = subscriptionRepository.findDueForBilling(LocalDate.now());

        for (Subscription subscription : dueSubscriptions) {
            try {
                // Aquí se podría crear una transacción automática
                log.info("Processing subscription {} for user {}",
                        subscription.getId(), subscription.getUserId());

                subscription.updateNextBillingDate();
                subscriptionRepository.save(subscription);

            } catch (Exception e) {
                log.error("Error processing subscription {}: {}",
                         subscription.getId(), e.getMessage());
            }
        }

        log.info("Finished processing due subscriptions");
    }
}

