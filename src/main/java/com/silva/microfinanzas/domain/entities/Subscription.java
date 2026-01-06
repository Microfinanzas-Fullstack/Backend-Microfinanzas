package com.silva.microfinanzas.domain.entities;

import com.silva.microfinanzas.domain.events.DomainEvent;
import com.silva.microfinanzas.domain.events.SubscriptionStatusChangedEvent;
import com.silva.microfinanzas.domain.valueobjects.Money;
import com.silva.microfinanzas.domain.valueobjects.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entidad de dominio que representa una Suscripción.
 *
 * Gestiona el seguimiento de suscripciones recurrentes (Netflix, Spotify, etc.)
 * y sus pagos periódicos.
 */
@Entity
@Table(name = "subscriptions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscription extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    /**
     * Monto de la suscripción.
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false)),
        @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false))
    })
    private Money amount;

    /**
     * Frecuencia de cobro en días (30 = mensual, 365 = anual).
     */
    @Column(name = "billing_cycle_days", nullable = false)
    private Integer billingCycleDays;

    /**
     * Fecha del próximo cobro.
     */
    @Column(name = "next_billing_date", nullable = false)
    private LocalDate nextBillingDate;

    /**
     * Estado de la suscripción.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubscriptionStatus status;

    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private Subscription(Long userId, String name, String description,
                        Money amount, Integer billingCycleDays, LocalDate nextBillingDate) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.billingCycleDays = billingCycleDays;
        this.nextBillingDate = nextBillingDate;
        this.status = SubscriptionStatus.ACTIVE;
    }

    /**
     * Factory method para crear una nueva suscripción.
     */
    public static Subscription create(Long userId, String name, String description,
                                     Money amount, Integer billingCycleDays,
                                     LocalDate nextBillingDate) {
        validateUserId(userId);
        validateName(name);
        validateAmount(amount);
        validateBillingCycleDays(billingCycleDays);
        validateNextBillingDate(nextBillingDate);

        return new Subscription(userId, name, description, amount,
                              billingCycleDays, nextBillingDate);
    }

    /**
     * Pausa la suscripción.
     */
    public void pause() {
        if (this.status != SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Only active subscriptions can be paused");
        }

        SubscriptionStatus previousStatus = this.status;
        this.status = SubscriptionStatus.PAUSED;

        registerEvent(new SubscriptionStatusChangedEvent(
            this.getId(), this.userId, previousStatus, this.status
        ));
    }

    /**
     * Cancela la suscripción.
     */
    public void cancel() {
        if (this.status == SubscriptionStatus.CANCELLED) {
            throw new IllegalStateException("Subscription is already cancelled");
        }

        SubscriptionStatus previousStatus = this.status;
        this.status = SubscriptionStatus.CANCELLED;

        registerEvent(new SubscriptionStatusChangedEvent(
            this.getId(), this.userId, previousStatus, this.status
        ));
    }

    /**
     * Reactiva la suscripción.
     */
    public void reactivate() {
        if (!this.status.canBeReactivated()) {
            throw new IllegalStateException(
                "Subscription in status " + this.status + " cannot be reactivated"
            );
        }

        SubscriptionStatus previousStatus = this.status;
        this.status = SubscriptionStatus.ACTIVE;

        registerEvent(new SubscriptionStatusChangedEvent(
            this.getId(), this.userId, previousStatus, this.status
        ));
    }

    /**
     * Marca la suscripción como expirada.
     */
    public void markAsExpired() {
        SubscriptionStatus previousStatus = this.status;
        this.status = SubscriptionStatus.EXPIRED;

        registerEvent(new SubscriptionStatusChangedEvent(
            this.getId(), this.userId, previousStatus, this.status
        ));
    }

    /**
     * Actualiza la fecha del próximo cobro (después de procesar un pago).
     */
    public void updateNextBillingDate() {
        this.nextBillingDate = this.nextBillingDate.plusDays(this.billingCycleDays);
    }

    /**
     * Verifica si la suscripción debe ser cobrada hoy.
     */
    public boolean isDueForBilling() {
        return this.status.isActive()
            && !this.nextBillingDate.isAfter(LocalDate.now());
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

    private static void validateUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }

    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }

    private static void validateAmount(Money amount) {
        if (amount == null || !amount.isPositive()) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    private static void validateBillingCycleDays(Integer days) {
        if (days == null || days <= 0) {
            throw new IllegalArgumentException("Billing cycle days must be positive");
        }
    }

    private static void validateNextBillingDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Next billing date cannot be null");
        }
    }
}

