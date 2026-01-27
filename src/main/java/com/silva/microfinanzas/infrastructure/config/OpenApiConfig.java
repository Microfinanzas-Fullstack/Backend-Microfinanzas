package com.silva.microfinanzas.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI/Swagger para documentación de la API.
 *
 * Esta clase configura la documentación interactiva de la API usando SpringDoc
 * OpenAPI.
 * Incluye configuración de seguridad JWT para poder probar endpoints
 * protegidos.
 */
@Configuration
public class OpenApiConfig {

        /**
         * Configuración personalizada de OpenAPI.
         * Define información general de la API y configuración de seguridad JWT.
         */
        @Bean
        public OpenAPI customOpenAPI() {
                final String securitySchemeName = "Bearer Authentication";

                return new OpenAPI()
                                .info(new Info()
                                                .title("Microfinanzas API")
                                                .version("1.0.0")
                                                .description("API REST para gestión de microfinanzas personales basada en Domain-Driven Design (DDD).\n\n"
                                                                +
                                                                "### Características:\n" +
                                                                "- Gestión de usuarios con autenticación JWT\n" +
                                                                "- Registro de ingresos y gastos (transacciones)\n" +
                                                                "- Seguimiento de suscripciones\n" +
                                                                "- Arquitectura DDD (Domain-Driven Design)\n" +
                                                                "- Seguridad con Spring Security + JWT\n\n" +
                                                                "### Autenticación:\n" +
                                                                "1. Registra un usuario usando `/api/auth/register`\n" +
                                                                "2. Inicia sesión usando `/api/auth/login` para obtener el token JWT\n"
                                                                +
                                                                "3. Haz clic en el botón **Authorize** (🔒) arriba\n" +
                                                                "4. Ingresa tu token en el formato: `Bearer <tu-token-aqui>`\n"
                                                                +
                                                                "5. Ahora puedes probar los endpoints protegidos")
                                                .contact(new Contact()
                                                                .name("Silva Industries")
                                                                .email("silva@example.com")
                                                                .url("https://github.com/SebasSilvaT"))
                                                .license(new License()
                                                                .name("Apache 2.0")
                                                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                                .addSecurityItem(new SecurityRequirement()
                                                .addList(securitySchemeName))
                                .components(new Components()
                                                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                                                .name(securitySchemeName)
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")
                                                                .description("Ingresa tu token JWT en el formato: Bearer <token>")));
        }
}
