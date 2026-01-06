package com.silva.microfinanzas.application.dtos;

import com.silva.microfinanzas.domain.valueobjects.TransactionType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para crear una nueva transacción.
 *
 * Los DTOs (Data Transfer Objects) son objetos que transfieren datos
 * entre la capa de aplicación y la capa de presentación (API REST).
 *
 * Incluyen validaciones usando Bean Validation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionDTO {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be 3 characters")
    private String currency;

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    @NotBlank(message = "Category is required")
    @Size(min = 1, max = 100, message = "Category must be between 1 and 100 characters")
    private String category;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Transaction date is required")
    @PastOrPresent(message = "Transaction date cannot be in the future")
    private LocalDate transactionDate;
}

