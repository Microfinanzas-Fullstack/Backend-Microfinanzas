package com.silva.microfinanzas.domain.specifications;

import com.silva.microfinanzas.domain.aggregates.Transaction;
import com.silva.microfinanzas.domain.valueobjects.TransactionType;

/**
 * Especificación que verifica si una transacción es de un tipo específico.
 */
public class TransactionTypeSpecification implements Specification<Transaction> {

    private final TransactionType requiredType;

    public TransactionTypeSpecification(TransactionType requiredType) {
        if (requiredType == null) {
            throw new IllegalArgumentException("Required type cannot be null");
        }
        this.requiredType = requiredType;
    }

    @Override
    public boolean isSatisfiedBy(Transaction transaction) {
        return transaction != null
            && transaction.getType() != null
            && transaction.getType().equals(requiredType);
    }
}

