package com.silva.microfinanzas.infrastructure.rest;

import com.silva.microfinanzas.application.dtos.JwtResponseDTO;
import com.silva.microfinanzas.application.dtos.LoginRequestDTO;
import com.silva.microfinanzas.application.dtos.RegisterUserDTO;
import com.silva.microfinanzas.application.dtos.UserResponseDTO;
import com.silva.microfinanzas.application.services.UserService;
import com.silva.microfinanzas.domain.entities.User;
import com.silva.microfinanzas.domain.repositories.UserRepository;
import com.silva.microfinanzas.infrastructure.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para autenticación y registro de usuarios.
 *
 * En la arquitectura hexagonal, los controladores forman parte
 * de la capa de infraestructura (adaptadores de entrada).
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints públicos para registro e inicio de sesión")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    /**
     * Endpoint para registro de nuevos usuarios.
     * POST /api/auth/register
     */
    @Operation(summary = "Registrar nuevo usuario", description = "Crea una nueva cuenta de usuario en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
                content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de registro inválidos"),
        @ApiResponse(responseCode = "409", description = "El email ya está registrado")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody RegisterUserDTO registerDTO) {
        log.info("Registering new user: {}", registerDTO.getEmail());

        UserResponseDTO userResponse = userService.registerUser(registerDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    /**
     * Endpoint para login de usuarios.
     * POST /api/auth/login
     */
    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y retorna un token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso",
                content = @Content(schema = @Schema(implementation = JwtResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        log.info("Login attempt for user: {}", loginRequest.getEmail());

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        // Obtener información del usuario
        User user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        JwtResponseDTO response = new JwtResponseDTO(
            jwt,
            user.getId(),
            user.getEmail(),
            user.getFullName()
        );

        log.info("User logged in successfully: {}", loginRequest.getEmail());

        return ResponseEntity.ok(response);
    }
}

