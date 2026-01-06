package com.silva.microfinanzas.domain.specifications;

import com.silva.microfinanzas.domain.aggregates.Transaction;
import com.silva.microfinanzas.domain.valueobjects.Money;

import java.math.BigDecimal;

/**
 * Especificación que verifica si una transacción excede un límite de monto.
 *
 * Ejemplo de uso del patrón Specification para encapsular reglas de negocio.
 * Esta especificación podría usarse para validaciones, alertas o reportes.
 */
public class TransactionExceedsAmountSpecification implements Specification<Transaction> {

    private final Money limitAmount;

    public TransactionExceedsAmountSpecification(Money limitAmount) {
        if (limitAmount == null) {
            throw new IllegalArgumentException("Limit amount cannot be null");
        }
        this.limitAmount = limitAmount;
    }

    @Override
    public boolean isSatisfiedBy(Transaction transaction) {
        if (transaction == null || transaction.getAmount() == null) {
            return false;
        }

        // Verifica que las monedas sean iguales antes de comparar
        if (!transaction.getAmount().getCurrency().equals(limitAmount.getCurrency())) {
            return false;
        }

        return transaction.getAmount().getAmount()
            .compareTo(limitAmount.getAmount()) > 0;
    }
}

