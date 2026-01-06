package com.silva.microfinanzas.domain.specifications;

import com.silva.microfinanzas.domain.aggregates.Transaction;

import java.time.LocalDate;

/**
 * Especificación que verifica si una transacción está dentro de un rango de fechas.
 *
 * Útil para filtrar transacciones por períodos (mes actual, año fiscal, etc.).
 */
public class TransactionDateRangeSpecification implements Specification<Transaction> {

    private final LocalDate startDate;
    private final LocalDate endDate;

    public TransactionDateRangeSpecification(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean isSatisfiedBy(Transaction transaction) {
        if (transaction == null || transaction.getTransactionDate() == null) {
            return false;
        }

        LocalDate transactionDate = transaction.getTransactionDate();
        return !transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate);
    }
}

