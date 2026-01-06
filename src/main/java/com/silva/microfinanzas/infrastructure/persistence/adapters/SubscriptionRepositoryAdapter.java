package com.silva.microfinanzas.infrastructure.persistence.adapters;

import com.silva.microfinanzas.domain.entities.Subscription;
import com.silva.microfinanzas.domain.repositories.SubscriptionRepository;
import com.silva.microfinanzas.domain.valueobjects.SubscriptionStatus;
import com.silva.microfinanzas.infrastructure.persistence.jpa.JpaSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador para SubscriptionRepository.
 */
@Component
@RequiredArgsConstructor
public class SubscriptionRepositoryAdapter implements SubscriptionRepository {

    private final JpaSubscriptionRepository jpaRepository;

    @Override
    public Subscription save(Subscription subscription) {
        return jpaRepository.save(subscription);
    }

    @Override
    public Optional<Subscription> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Subscription> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId);
    }

    @Override
    public List<Subscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status) {
        return jpaRepository.findByUserIdAndStatus(userId, status);
    }

    @Override
    public List<Subscription> findDueForBilling(LocalDate date) {
        return jpaRepository.findDueForBilling(date);
    }

    @Override
    public List<Subscription> findActiveByUserId(Long userId) {
        return jpaRepository.findActiveByUserId(userId);
    }

    @Override
    public void delete(Subscription subscription) {
        jpaRepository.delete(subscription);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}

