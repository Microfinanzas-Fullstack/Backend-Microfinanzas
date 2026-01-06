package com.silva.microfinanzas.infrastructure.persistence.adapters;

import com.silva.microfinanzas.domain.aggregates.Transaction;
import com.silva.microfinanzas.domain.repositories.TransactionRepository;
import com.silva.microfinanzas.domain.valueobjects.TransactionType;
import com.silva.microfinanzas.infrastructure.persistence.jpa.JpaTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa TransactionRepository del dominio
 * delegando a JpaTransactionRepository de Spring Data JPA.
 *
 * Este patrón (Adapter) permite mantener el dominio independiente
 * de la tecnología de persistencia específica.
 */
@Component
@RequiredArgsConstructor
public class TransactionRepositoryAdapter implements TransactionRepository {

    private final JpaTransactionRepository jpaRepository;

    @Override
    public Transaction save(Transaction transaction) {
        return jpaRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Transaction> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId);
    }

    @Override
    public List<Transaction> findByUserIdAndType(Long userId, TransactionType type) {
        return jpaRepository.findByUserIdAndType(userId, type);
    }

    @Override
    public List<Transaction> findByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return jpaRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
    }

    @Override
    public List<Transaction> findByUserIdAndCategory(Long userId, String category) {
        return jpaRepository.findByUserIdAndCategory(userId, category);
    }

    @Override
    public void delete(Transaction transaction) {
        jpaRepository.delete(transaction);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
