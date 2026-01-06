package com.silva.microfinanzas.infrastructure.rest;

import com.silva.microfinanzas.application.dtos.CreateTransactionDTO;
import com.silva.microfinanzas.application.dtos.TransactionResponseDTO;
import com.silva.microfinanzas.application.services.TransactionService;
import com.silva.microfinanzas.domain.entities.User;
import com.silva.microfinanzas.domain.repositories.UserRepository;
import com.silva.microfinanzas.domain.valueobjects.TransactionType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para gestión de transacciones.
 *
 * Expone endpoints para crear, leer, actualizar y eliminar transacciones.
 */
@Slf4j
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final UserRepository userRepository;

    /**
     * Crea una nueva transacción.
     * POST /api/transactions
     */
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @Valid @RequestBody CreateTransactionDTO transactionDTO) {
        Long userId = getCurrentUserId();

        TransactionResponseDTO response = transactionService.createTransaction(userId, transactionDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene todas las transacciones del usuario autenticado.
     * GET /api/transactions
     */
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getUserTransactions() {
        Long userId = getCurrentUserId();

        List<TransactionResponseDTO> transactions = transactionService.getUserTransactions(userId);

        return ResponseEntity.ok(transactions);
    }

    /**
     * Obtiene una transacción por ID.
     * GET /api/transactions/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Long id) {
        Long userId = getCurrentUserId();

        TransactionResponseDTO transaction = transactionService.getTransactionById(userId, id);

        return ResponseEntity.ok(transaction);
    }

    /**
     * Obtiene transacciones por tipo.
     * GET /api/transactions/type/{type}
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByType(
            @PathVariable TransactionType type) {
        Long userId = getCurrentUserId();

        List<TransactionResponseDTO> transactions =
            transactionService.getUserTransactionsByType(userId, type);

        return ResponseEntity.ok(transactions);
    }

    /**
     * Obtiene transacciones por rango de fechas.
     * GET /api/transactions/date-range?startDate=2024-01-01&endDate=2024-12-31
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = getCurrentUserId();

        List<TransactionResponseDTO> transactions =
            transactionService.getUserTransactionsByDateRange(userId, startDate, endDate);

        return ResponseEntity.ok(transactions);
    }

    /**
     * Obtiene transacciones por categoría.
     * GET /api/transactions/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByCategory(
            @PathVariable String category) {
        Long userId = getCurrentUserId();

        List<TransactionResponseDTO> transactions =
            transactionService.getUserTransactionsByCategory(userId, category);

        return ResponseEntity.ok(transactions);
    }

    /**
     * Actualiza una transacción.
     * PUT /api/transactions/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody CreateTransactionDTO transactionDTO) {
        Long userId = getCurrentUserId();

        TransactionResponseDTO response = transactionService.updateTransaction(userId, id, transactionDTO);

        return ResponseEntity.ok(response);
    }

    /**
     * Elimina una transacción.
     * DELETE /api/transactions/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        Long userId = getCurrentUserId();

        transactionService.deleteTransaction(userId, id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Método helper para obtener el ID del usuario autenticado.
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getId();
    }
}

