package com.silva.microfinanzas.application.dtos;

import com.silva.microfinanzas.domain.valueobjects.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para transacciones.
 *
 * Se usa para devolver información de transacciones a los clientes de la API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {

    private Long id;
    private BigDecimal amount;
    private String currency;
    private TransactionType type;
    private String category;
    private String description;
    private LocalDate transactionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

