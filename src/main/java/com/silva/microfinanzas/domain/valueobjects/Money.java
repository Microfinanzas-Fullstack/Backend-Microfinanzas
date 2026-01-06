package com.silva.microfinanzas.domain.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

/**
 * Value Object que representa dinero en el dominio.
 * Encapsula la cantidad y la moneda siguiendo el patrón Money de Martin Fowler.
 *
 * En DDD, los Value Objects son inmutables y se comparan por valor, no por identidad.
 * Este VO garantiza que toda operación monetaria incluya la moneda correspondiente.
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Money {

    private BigDecimal amount;
    private String currency;

    /**
     * Factory method para crear una instancia de Money.
     * Uso de factory methods en lugar de constructores públicos para mayor expresividad.
     */
    public static Money of(BigDecimal amount, String currency) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");

        if (amount.scale() > 2) {
            amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        return new Money(amount, currency);
    }

    /**
     * Factory method para crear Money en la moneda por defecto (MXN).
     */
    public static Money ofMXN(BigDecimal amount) {
        return of(amount, "MXN");
    }

    /**
     * Suma dos cantidades de dinero.
     * Validación de dominio: solo se pueden sumar cantidades de la misma moneda.
     */
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add money with different currencies");
        }
        return Money.of(this.amount.add(other.amount), this.currency);
    }

    /**
     * Resta dos cantidades de dinero.
     */
    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract money with different currencies");
        }
        return Money.of(this.amount.subtract(other.amount), this.currency);
    }

    /**
     * Verifica si el monto es positivo.
     */
    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Verifica si el monto es negativo.
     */
    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0 && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return String.format("%s %s", amount, currency);
    }
}

