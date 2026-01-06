package com.silva.microfinanzas.infrastructure.persistence.jpa;

import com.silva.microfinanzas.domain.entities.Subscription;
import com.silva.microfinanzas.domain.valueobjects.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio JPA para Subscription.
 */
@Repository
public interface JpaSubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByUserId(Long userId);

    List<Subscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status);

    @Query("SELECT s FROM Subscription s WHERE s.status = 'ACTIVE' " +
           "AND s.nextBillingDate <= :date")
    List<Subscription> findDueForBilling(@Param("date") LocalDate date);

    @Query("SELECT s FROM Subscription s WHERE s.userId = :userId " +
           "AND s.status = 'ACTIVE'")
    List<Subscription> findActiveByUserId(@Param("userId") Long userId);
}

