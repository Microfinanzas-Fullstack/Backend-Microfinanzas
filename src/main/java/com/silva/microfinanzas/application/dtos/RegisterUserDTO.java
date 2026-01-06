package com.silva.microfinanzas.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para registro de usuarios.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos requeridos para registrar un nuevo usuario")
public class RegisterUserDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(description = "Email del usuario (debe ser único)", example = "nuevo.usuario@example.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "Contraseña del usuario (mínimo 6 caracteres)", example = "miPassword123")
    private String password;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String fullName;
}

