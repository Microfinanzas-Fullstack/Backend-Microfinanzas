package com.silva.microfinanzas.infrastructure.rest;

import com.silva.microfinanzas.application.dtos.CreateSubscriptionDTO;
import com.silva.microfinanzas.application.dtos.SubscriptionResponseDTO;
import com.silva.microfinanzas.application.services.SubscriptionService;
import com.silva.microfinanzas.domain.entities.User;
import com.silva.microfinanzas.domain.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de suscripciones.
 */
@Slf4j
@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserRepository userRepository;

    /**
     * Crea una nueva suscripción.
     * POST /api/subscriptions
     */
    @PostMapping
    public ResponseEntity<SubscriptionResponseDTO> createSubscription(
            @Valid @RequestBody CreateSubscriptionDTO subscriptionDTO) {
        Long userId = getCurrentUserId();

        SubscriptionResponseDTO response = subscriptionService.createSubscription(userId, subscriptionDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene todas las suscripciones del usuario.
     * GET /api/subscriptions
     */
    @GetMapping
    public ResponseEntity<List<SubscriptionResponseDTO>> getUserSubscriptions() {
        Long userId = getCurrentUserId();

        List<SubscriptionResponseDTO> subscriptions = subscriptionService.getUserSubscriptions(userId);

        return ResponseEntity.ok(subscriptions);
    }

    /**
     * Obtiene las suscripciones activas del usuario.
     * GET /api/subscriptions/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<SubscriptionResponseDTO>> getActiveSubscriptions() {
        Long userId = getCurrentUserId();

        List<SubscriptionResponseDTO> subscriptions = subscriptionService.getActiveSubscriptions(userId);

        return ResponseEntity.ok(subscriptions);
    }

    /**
     * Obtiene una suscripción por ID.
     * GET /api/subscriptions/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDTO> getSubscriptionById(@PathVariable Long id) {
        Long userId = getCurrentUserId();

        SubscriptionResponseDTO subscription = subscriptionService.getSubscriptionById(userId, id);

        return ResponseEntity.ok(subscription);
    }

    /**
     * Pausa una suscripción.
     * PUT /api/subscriptions/{id}/pause
     */
    @PutMapping("/{id}/pause")
    public ResponseEntity<SubscriptionResponseDTO> pauseSubscription(@PathVariable Long id) {
        Long userId = getCurrentUserId();

        SubscriptionResponseDTO response = subscriptionService.pauseSubscription(userId, id);

        return ResponseEntity.ok(response);
    }

    /**
     * Cancela una suscripción.
     * PUT /api/subscriptions/{id}/cancel
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<SubscriptionResponseDTO> cancelSubscription(@PathVariable Long id) {
        Long userId = getCurrentUserId();

        SubscriptionResponseDTO response = subscriptionService.cancelSubscription(userId, id);

        return ResponseEntity.ok(response);
    }

    /**
     * Reactiva una suscripción.
     * PUT /api/subscriptions/{id}/reactivate
     */
    @PutMapping("/{id}/reactivate")
    public ResponseEntity<SubscriptionResponseDTO> reactivateSubscription(@PathVariable Long id) {
        Long userId = getCurrentUserId();

        SubscriptionResponseDTO response = subscriptionService.reactivateSubscription(userId, id);

        return ResponseEntity.ok(response);
    }

    /**
     * Elimina una suscripción.
     * DELETE /api/subscriptions/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        Long userId = getCurrentUserId();

        subscriptionService.deleteSubscription(userId, id);

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

