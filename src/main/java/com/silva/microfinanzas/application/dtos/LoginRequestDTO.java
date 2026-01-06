package com.silva.microfinanzas.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para login de usuarios.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos requeridos para iniciar sesión")
public class LoginRequestDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Email del usuario", example = "usuario@example.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;
}

