package com.silva.microfinanzas.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para JWT tokens.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDTO {

    private String token;
    private String type = "Bearer";
    private Long userId;
    private String email;
    private String fullName;

    public JwtResponseDTO(String token, Long userId, String email, String fullName) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
    }
}

