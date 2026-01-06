package com.silva.microfinanzas.domain.repositories;

import com.silva.microfinanzas.domain.aggregates.Transaction;
import com.silva.microfinanzas.domain.valueobjects.TransactionType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface del dominio para Transaction.
 *
 * En DDD, los repositories son interfaces definidas en el dominio
 * pero implementadas en la capa de infraestructura.
 *
 * Este contrato define las operaciones de persistencia necesarias
 * sin exponer detalles de implementación (JPA, JDBC, etc.).
 */
public interface TransactionRepository {

    /**
     * Guarda una transacción (crear o actualizar).
     */
    Transaction save(Transaction transaction);

    /**
     * Busca una transacción por su ID.
     */
    Optional<Transaction> findById(Long id);

    /**
     * Busca todas las transacciones de un usuario.
     */
    List<Transaction> findByUserId(Long userId);

    /**
     * Busca transacciones de un usuario por tipo.
     */
    List<Transaction> findByUserIdAndType(Long userId, TransactionType type);

    /**
     * Busca transacciones de un usuario en un rango de fechas.
     */
    List<Transaction> findByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * Busca transacciones de un usuario por categoría.
     */
    List<Transaction> findByUserIdAndCategory(Long userId, String category);

    /**
     * Elimina una transacción.
     */
    void delete(Transaction transaction);

    /**
     * Elimina una transacción por ID.
     */
    void deleteById(Long id);

    /**
     * Verifica si existe una transacción con el ID dado.
     */
    boolean existsById(Long id);
}

