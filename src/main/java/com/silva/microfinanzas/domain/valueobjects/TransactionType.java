package com.silva.microfinanzas.domain.valueobjects;

/**
 * Enumeración que representa los tipos de transacción en el dominio.
 *
 * En DDD, las enumeraciones son Value Objects que representan
 * un conjunto cerrado de valores del dominio de negocio.
 */
public enum TransactionType {
    /**
     * Representa un ingreso (dinero que entra).
     */
    INCOME("Ingreso"),

    /**
     * Representa un gasto (dinero que sale).
     */
    EXPENSE("Gasto");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Verifica si el tipo de transacción es un ingreso.
     */
    public boolean isIncome() {
        return this == INCOME;
    }

    /**
     * Verifica si el tipo de transacción es un gasto.
     */
    public boolean isExpense() {
        return this == EXPENSE;
    }
}

