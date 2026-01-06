package com.silva.microfinanzas.application.services;

import com.silva.microfinanzas.application.dtos.CreateTransactionDTO;
import com.silva.microfinanzas.application.dtos.TransactionResponseDTO;
import com.silva.microfinanzas.application.mappers.TransactionMapper;
import com.silva.microfinanzas.domain.aggregates.Transaction;
import com.silva.microfinanzas.domain.events.DomainEventPublisher;
import com.silva.microfinanzas.domain.repositories.TransactionRepository;
import com.silva.microfinanzas.domain.valueobjects.Money;
import com.silva.microfinanzas.domain.valueobjects.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de aplicación para gestionar transacciones.
 *
 * En DDD, los servicios de aplicación orquestan casos de uso y coordinan
 * entre el dominio y la infraestructura. No contienen lógica de negocio,
 * esa lógica reside en las entidades de dominio.
 *
 * Responsabilidades:
 * - Coordinar operaciones entre múltiples agregados
 * - Gestionar transacciones de base de datos
 * - Publicar eventos de dominio
 * - Convertir entre DTOs y entidades de dominio
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final DomainEventPublisher eventPublisher;

    /**
     * Crea una nueva transacción.
     */
    public TransactionResponseDTO createTransaction(Long userId, CreateTransactionDTO dto) {
        log.info("Creating transaction for user: {}", userId);

        // Crear el Value Object Money
        Money amount = Money.of(dto.getAmount(), dto.getCurrency());

        // Usar el factory method del Aggregate Root para crear la transacción
        Transaction transaction = Transaction.create(
            userId,
            amount,
            dto.getType(),
            dto.getCategory(),
            dto.getDescription(),
            dto.getTransactionDate()
        );

        // Persistir la transacción
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Publicar eventos de dominio
        savedTransaction.getDomainEvents().forEach(eventPublisher::publish);
        savedTransaction.clearDomainEvents();

        log.info("Transaction created successfully with ID: {}", savedTransaction.getId());

        return transactionMapper.toResponseDTO(savedTransaction);
    }

    /**
     * Obtiene una transacción por ID.
     */
    @Transactional(readOnly = true)
    public TransactionResponseDTO getTransactionById(Long userId, Long transactionId) {
        log.debug("Getting transaction {} for user {}", transactionId, userId);

        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Verificar que la transacción pertenece al usuario
        if (!transaction.getUserId().equals(userId)) {
            throw new RuntimeException("Transaction does not belong to user");
        }

        return transactionMapper.toResponseDTO(transaction);
    }

    /**
     * Obtiene todas las transacciones de un usuario.
     */
    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getUserTransactions(Long userId) {
        log.debug("Getting all transactions for user {}", userId);

        return transactionRepository.findByUserId(userId).stream()
            .map(transactionMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene transacciones de un usuario por tipo.
     */
    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getUserTransactionsByType(Long userId, TransactionType type) {
        log.debug("Getting transactions of type {} for user {}", type, userId);

        return transactionRepository.findByUserIdAndType(userId, type).stream()
            .map(transactionMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene transacciones de un usuario en un rango de fechas.
     */
    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getUserTransactionsByDateRange(
            Long userId, LocalDate startDate, LocalDate endDate) {
        log.debug("Getting transactions for user {} between {} and {}", userId, startDate, endDate);

        return transactionRepository.findByUserIdAndDateRange(userId, startDate, endDate).stream()
            .map(transactionMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene transacciones de un usuario por categoría.
     */
    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getUserTransactionsByCategory(Long userId, String category) {
        log.debug("Getting transactions of category {} for user {}", category, userId);

        return transactionRepository.findByUserIdAndCategory(userId, category).stream()
            .map(transactionMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Actualiza una transacción.
     */
    public TransactionResponseDTO updateTransaction(Long userId, Long transactionId, CreateTransactionDTO dto) {
        log.info("Updating transaction {} for user {}", transactionId, userId);

        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUserId().equals(userId)) {
            throw new RuntimeException("Transaction does not belong to user");
        }

        Money newAmount = Money.of(dto.getAmount(), dto.getCurrency());
        transaction.update(newAmount, dto.getCategory(), dto.getDescription());

        Transaction updatedTransaction = transactionRepository.save(transaction);

        log.info("Transaction updated successfully");

        return transactionMapper.toResponseDTO(updatedTransaction);
    }

    /**
     * Elimina una transacción.
     */
    public void deleteTransaction(Long userId, Long transactionId) {
        log.info("Deleting transaction {} for user {}", transactionId, userId);

        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUserId().equals(userId)) {
            throw new RuntimeException("Transaction does not belong to user");
        }

        transactionRepository.delete(transaction);

        log.info("Transaction deleted successfully");
    }
}

