package com.silva.microfinanzas.domain.valueobjects;

/**
 * Enumeración que representa los estados de una suscripción.
 *
 * Representa el ciclo de vida completo de una suscripción en el dominio.
 */
public enum SubscriptionStatus {
    /**
     * Suscripción activa y vigente.
     */
    ACTIVE("Activa"),

    /**
     * Suscripción pausada temporalmente.
     */
    PAUSED("Pausada"),

    /**
     * Suscripción cancelada por el usuario.
     */
    CANCELLED("Cancelada"),

    /**
     * Suscripción expirada por falta de pago.
     */
    EXPIRED("Expirada");

    private final String displayName;

    SubscriptionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Verifica si la suscripción está activa.
     */
    public boolean isActive() {
        return this == ACTIVE;
    }

    /**
     * Verifica si la suscripción puede ser reactivada.
     */
    public boolean canBeReactivated() {
        return this == PAUSED || this == EXPIRED;
    }
}

