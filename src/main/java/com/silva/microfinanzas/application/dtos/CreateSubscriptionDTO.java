package com.silva.microfinanzas.application.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para crear una nueva suscripción.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubscriptionDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be 3 characters")
    private String currency;

    @NotNull(message = "Billing cycle days is required")
    @Positive(message = "Billing cycle days must be positive")
    private Integer billingCycleDays;

    @NotNull(message = "Next billing date is required")
    @FutureOrPresent(message = "Next billing date must be today or in the future")
    private LocalDate nextBillingDate;
}

