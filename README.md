# 🏦 Microfinanzas - Silva Industries

¡Bienvenido al núcleo de **Gestión de Microfinanzas**! Este microservicio es el motor financiero de nuestra plataforma, diseñado para ser sólido, escalable y, sobre todo, fácil de entender.

Aquí no solo escribimos código; aplicamos **Arquitectura Hexagonal** y **Domain-Driven Design (DDD)** para asegurar que la lógica de negocio esté protegida y siempre sea la prioridad.

## ✨ ¿Qué hace especial a este proyecto?

Este no es el típico CRUD. Hemos puesto mucho corazón en la estructura:
*   **Lenguaje Ubicuo:** Si hablas con un contador o un administrador, entenderán nuestro código. Usamos conceptos como `Aggregates`, `Value Objects` (como `Money`) y `Specifications`.
*   **Arquitectura Limpia:** El dominio es sagrado. No verás anotaciones de bases de datos o frameworks ensuciando nuestras reglas de negocio.
*   **Seguridad Primero:** Todo viaja cifrado con BCrypt y protegido por tokens JWT de alta seguridad.

## 🏗️ Nuestra Casa (Arquitectura)

Dividimos todo en tres capas claras:
1.  **Dominio (El Cerebro):** Donde viven las reglas de oro. Validaciones de montos, estados de suscripciones y eventos.
2.  **Aplicación (El Director de Orquesta):** Coordina los casos de uso, transformando datos y disparando procesos.
3.  **Infraestructura (Las Manos):** Se encarga de lo sucio: hablar con MySQL, gestionar el JWT y exponer la API REST.

---

## 🚀 Guía Rápida de Despegue

### 1. Prepara el terreno (Base de Datos)
Necesitas una instancia de MySQL. Crea la base de datos con:
```sql
CREATE DATABASE microfinanzas;
```

### 2. Enciende los motores
Desde la raíz de la carpeta `/Microfinanzas`:
```bash
mvn spring-boot:run
```
La API cobrará vida en: `http://localhost:8080`

### 3. Explora la API (Swagger)
Para ver todos los endpoints en acción y probarlos visualmente, ve a:
👉 `http://localhost:8080/swagger-ui.html`

---

## 📡 Endpoints Clave para Empezar

*   **Registro:** `POST /api/auth/register` (Crea tu cuenta y empieza a gestionar).
*   **Login:** `POST /api/auth/login` (Obtén tu token de acceso).
*   **Transacciones:** `GET /api/transactions` (Mira tus ingresos y gastos).
*   **Suscripciones:** `POST /api/subscriptions` (Controla esos pagos recurrentes).

## 🛠️ Tecnologías que nos respaldan
*   **Java 17 & Spring Boot 3** (Potencia y modernidad).
*   **MySQL & JPA** (Persistencia confiable).
*   **MapStruct & Lombok** (Código limpio y sin boilerplate).
*   **JWT & Security** (Tu dinero y datos, a salvo).

---
**Desarrollado con ❤️ por Silva Industries.**
"Simplificando tus finanzas, un pixel a la vez."


