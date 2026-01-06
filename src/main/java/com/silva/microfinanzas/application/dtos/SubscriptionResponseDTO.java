package com.silva.microfinanzas.application.dtos;

import com.silva.microfinanzas.domain.valueobjects.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para suscripciones.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponseDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal amount;
    private String currency;
    private Integer billingCycleDays;
    private LocalDate nextBillingDate;
    private SubscriptionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

