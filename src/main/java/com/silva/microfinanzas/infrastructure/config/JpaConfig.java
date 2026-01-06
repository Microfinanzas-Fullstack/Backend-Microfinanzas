package com.silva.microfinanzas.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuración de JPA.
 *
 * EnableJpaAuditing habilita el auditing automático de entidades
 * (createdAt, updatedAt, etc.).
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
