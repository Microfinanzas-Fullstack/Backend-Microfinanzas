package com.silva.microfinanzas.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO de respuesta para usuarios.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String email;
    private String fullName;
    private Boolean enabled;
    private Set<String> roles;
    private LocalDateTime createdAt;
}

