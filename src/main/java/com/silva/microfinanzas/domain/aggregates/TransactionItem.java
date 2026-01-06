package com.silva.microfinanzas.domain.aggregates;

import com.silva.microfinanzas.domain.entities.BaseEntity;
import com.silva.microfinanzas.domain.valueobjects.Money;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un ítem individual dentro de una transacción.
 *
 * Esta es una entidad dentro del Aggregate Transaction, no es un Aggregate Root.
 * Solo puede ser accedida y modificada a través de Transaction.
 *
 * Útil para transacciones que tienen múltiples conceptos
 * (ej: una compra con varios productos).
 */
@Entity
@Table(name = "transaction_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransactionItem extends BaseEntity {

    /**
     * Referencia a la transacción padre.
     * Esta entidad no puede existir sin su Aggregate Root.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    @Setter(AccessLevel.PACKAGE) // Solo Transaction puede modificar esto
    private Transaction transaction;

    /**
     * Descripción del ítem.
     */
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * Monto del ítem.
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false)),
        @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false))
    })
    private Money amount;

    /**
     * Cantidad del ítem.
     */
    @Column(name = "quantity")
    private Integer quantity;

    private TransactionItem(String description, Money amount, Integer quantity) {
        this.description = description;
        this.amount = amount;
        this.quantity = quantity;
    }

    /**
     * Factory method para crear un TransactionItem.
     */
    public static TransactionItem create(String description, Money amount, Integer quantity) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        if (amount == null || !amount.isPositive()) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (quantity != null && quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        return new TransactionItem(description, amount, quantity);
    }
}

