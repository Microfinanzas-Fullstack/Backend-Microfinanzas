package com.silva.microfinanzas.infrastructure.persistence.jpa;

import com.silva.microfinanzas.domain.aggregates.Transaction;
import com.silva.microfinanzas.domain.valueobjects.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio JPA para Transaction.
 *
 * Spring Data JPA genera automáticamente la implementación.
 * Esta interface extiende JpaRepository que proporciona operaciones CRUD básicas.
 */
@Repository
public interface JpaTransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByUserIdAndType(Long userId, TransactionType type);

    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
           "AND t.transactionDate >= :startDate AND t.transactionDate <= :endDate " +
           "ORDER BY t.transactionDate DESC")
    List<Transaction> findByUserIdAndDateBetween(
        @Param("userId") Long userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    List<Transaction> findByUserIdAndCategory(Long userId, String category);
}
