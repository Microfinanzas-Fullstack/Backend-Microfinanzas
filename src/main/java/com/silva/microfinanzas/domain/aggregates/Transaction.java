package com.silva.microfinanzas.domain.aggregates;

import com.silva.microfinanzas.domain.entities.BaseEntity;
import com.silva.microfinanzas.domain.events.DomainEvent;
import com.silva.microfinanzas.domain.events.TransactionCreatedEvent;
import com.silva.microfinanzas.domain.valueobjects.Money;
import com.silva.microfinanzas.domain.valueobjects.TransactionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Aggregate Root para Transaction.
 *
 * En DDD, un Aggregate es un cluster de objetos de dominio que se tratan como una unidad
 * para cambios de datos. El Aggregate Root es la única entrada al Aggregate y garantiza
 * las invariantes del negocio.
 *
 * Transaction es el Aggregate Root que gestiona todas las operaciones relacionadas con
 * transacciones financieras (ingresos y gastos). Mantiene la consistencia y las reglas
 * de negocio relacionadas con las transacciones.
 *
 * Responsabilidades:
 * - Garantizar que todas las transacciones tengan un monto válido
 * - Asegurar que las categorías sean apropiadas
 * - Emitir eventos de dominio cuando ocurren cambios importantes
 * - Gestionar el ciclo de vida de TransactionItems asociados
 */
@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction extends BaseEntity {

    /**
     * Referencia al usuario propietario de la transacción.
     * En un diseño DDD, esto podría ser un UserID (Value Object),
     * pero por simplicidad usamos Long.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * Monto de la transacción como Value Object.
     * Embedded para que se persista en las mismas columnas de la tabla.
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false)),
        @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false))
    })
    private Money amount;

    /**
     * Tipo de transacción (INCOME o EXPENSE).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    /**
     * Categoría de la transacción (ej: "Alimentación", "Transporte", "Salario").
     */
    @Column(name = "category", nullable = false)
    private String category;

    /**
     * Descripción opcional de la transacción.
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * Fecha en que ocurrió la transacción.
     */
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    /**
     * Ítems de la transacción (relación uno a muchos).
     * Cascade ALL garantiza que los items se gestionen junto con la transacción.
     * orphanRemoval = true elimina items que ya no están en la colección.
     */
    @OneToMany(
        mappedBy = "transaction",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<TransactionItem> items = new ArrayList<>();

    /**
     * Eventos de dominio pendientes de publicar.
     * Transient porque no se persiste en la BD.
     */
    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    /**
     * Constructor privado para forzar el uso del factory method.
     * Patrón: Rich Domain Model - la creación se controla desde el dominio.
     */
    private Transaction(Long userId, Money amount, TransactionType type,
                       String category, String description, LocalDate transactionDate) {
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.description = description;
        this.transactionDate = transactionDate;
    }

    /**
     * Factory method para crear una nueva transacción.
     *
     * Este método encapsula las reglas de negocio para la creación:
     * - Valida que el monto sea positivo
     * - Valida que los datos requeridos no sean nulos
     * - Emite el evento de dominio TransactionCreatedEvent
     *
     * @throws IllegalArgumentException si las invariantes no se cumplen
     */
    public static Transaction create(Long userId, Money amount, TransactionType type,
                                     String category, String description,
                                     LocalDate transactionDate) {
        // Validaciones de negocio (Invariantes del Aggregate)
        validateUserId(userId);
        validateAmount(amount);
        validateType(type);
        validateCategory(category);
        validateTransactionDate(transactionDate);

        Transaction transaction = new Transaction(
            userId, amount, type, category, description, transactionDate
        );

        // Registrar evento de dominio (será publicado por el repositorio)
        transaction.registerEvent(
            new TransactionCreatedEvent(
                transaction.getId(),
                userId,
                amount,
                type,
                category
            )
        );

        return transaction;
    }

    /**
     * Agrega un item a la transacción.
     * Método que mantiene la consistencia bidireccional de la relación.
     */
    public void addItem(TransactionItem item) {
        items.add(item);
        item.setTransaction(this);
    }

    /**
     * Remueve un item de la transacción.
     */
    public void removeItem(TransactionItem item) {
        items.remove(item);
        item.setTransaction(null);
    }

    /**
     * Actualiza la transacción.
     * Aplica validaciones antes de actualizar.
     */
    public void update(Money newAmount, String newCategory, String newDescription) {
        validateAmount(newAmount);
        validateCategory(newCategory);

        this.amount = newAmount;
        this.category = newCategory;
        this.description = newDescription;
    }

    /**
     * Retorna una copia inmutable de los items.
     * Protege el encapsulamiento del aggregate.
     */
    public List<TransactionItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Retorna una copia inmutable de los eventos pendientes.
     */
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Limpia los eventos después de ser publicados.
     */
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    /**
     * Registra un evento de dominio para ser publicado.
     */
    private void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    // ============= Métodos de Validación (Invariantes) =============

    private static void validateUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }

    private static void validateAmount(Money amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (!amount.isPositive()) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    private static void validateType(TransactionType type) {
        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
    }

    private static void validateCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
    }

    private static void validateTransactionDate(LocalDate transactionDate) {
        if (transactionDate == null) {
            throw new IllegalArgumentException("Transaction date cannot be null");
        }
        if (transactionDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Transaction date cannot be in the future");
        }
    }

    // ============= Métodos de Negocio =============

    /**
     * Calcula el total de todos los items.
     */
    public Money calculateTotal() {
        if (items.isEmpty()) {
            return amount;
        }

        Money total = items.get(0).getAmount();
        for (int i = 1; i < items.size(); i++) {
            total = total.add(items.get(i).getAmount());
        }
        return total;
    }

    /**
     * Verifica si es un ingreso.
     */
    public boolean isIncome() {
        return type.isIncome();
    }

    /**
     * Verifica si es un gasto.
     */
    public boolean isExpense() {
        return type.isExpense();
    }
}

