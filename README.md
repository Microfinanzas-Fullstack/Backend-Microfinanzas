# Microservicio de Gestión de Microfinanzas

Microservicio construido con **Java 17**, **Spring Boot 3** y **Maven**, siguiendo los principios de **Domain-Driven Design (DDD)** y arquitectura hexagonal.

## 🏗️ Arquitectura

El proyecto está organizado en **tres capas principales** siguiendo DDD:

### 1. **Domain Layer** (Núcleo del Negocio)
Contiene la lógica de negocio pura, independiente de frameworks y tecnologías externas.

```
domain/
├── aggregates/          # Aggregate Roots (Transaction, TransactionItem)
├── entities/           # Entidades de dominio (User, Subscription, BaseEntity)
├── valueobjects/       # Value Objects (Money, TransactionType, SubscriptionStatus)
├── events/             # Domain Events (TransactionCreatedEvent, UserRegisteredEvent, etc.)
├── repositories/       # Interfaces de repositorios (contratos)
└── specifications/     # Patrón Specification para reglas de negocio
```

**Conceptos DDD implementados:**
- **Aggregate Roots**: `Transaction` con su entidad `TransactionItem`
- **Value Objects**: `Money` (inmutable, comparado por valor)
- **Domain Events**: Eventos para comunicación entre agregados
- **Specifications**: Patrones para encapsular reglas de negocio complejas
- **Repository Interfaces**: Definidas en el dominio, implementadas en infraestructura

### 2. **Application Layer** (Casos de Uso)
Orquesta las operaciones entre el dominio y la infraestructura.

```
application/
├── dtos/              # Data Transfer Objects
├── mappers/           # MapStruct mappers (Domain ↔ DTO)
└── services/          # Application Services (TransactionService, UserService, SubscriptionService)
```

**Responsabilidades:**
- Coordinar casos de uso
- Gestionar transacciones de base de datos
- Publicar eventos de dominio
- Mapear entre DTOs y entidades

### 3. **Infrastructure Layer** (Adaptadores)
Implementa los detalles técnicos y se conecta con el mundo exterior.

```
infrastructure/
├── persistence/
│   ├── jpa/          # Spring Data JPA Repositories
│   └── adapters/     # Implementaciones de repositorios del dominio
├── rest/             # REST Controllers
├── security/         # JWT, Spring Security
└── config/           # Configuraciones de Spring
```

## 🎯 Funcionalidades

### Gestión de Usuarios
- Registro de usuarios con encriptación de contraseñas
- Autenticación mediante JWT
- Roles y permisos

### Gestión de Transacciones (Ingresos/Gastos)
- Crear transacciones con validación de dominio
- Consultar por tipo (INCOME/EXPENSE)
- Filtrar por rango de fechas
- Filtrar por categoría
- Actualizar y eliminar transacciones
- Transacciones con múltiples items

### Seguimiento de Suscripciones
- Crear suscripciones recurrentes
- Pausar, cancelar y reactivar suscripciones
- Consultar suscripciones activas
- Procesamiento automático de cobros

## 🔒 Seguridad

- **Spring Security** con autenticación JWT
- Contraseñas encriptadas con **BCrypt**
- Tokens JWT con expiración configurable
- Endpoints protegidos por defecto
- Validación de pertenencia de recursos (un usuario solo accede a sus datos)

## 📦 Dependencias Principales

```xml
<dependencies>
    <!-- Spring Boot -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- MySQL -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
    </dependency>
    
    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.12.3</version>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    
    <!-- MapStruct -->
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>1.5.5.Final</version>
    </dependency>
</dependencies>
```

## ⚙️ Configuración

### Base de Datos MySQL

Configura las credenciales en `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/microfinanzas?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### JWT

```properties
jwt.secret=5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437
jwt.expiration=86400000
jwt.refresh-expiration=604800000
```

## 🚀 Ejecución

### 1. Crear la base de datos MySQL

```sql
CREATE DATABASE microfinanzas;
```

### 2. Compilar el proyecto

```bash
mvn clean install
```

### 3. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

O ejecutar el JAR:

```bash
java -jar target/Microfinanzas-0.0.1-SNAPSHOT.jar
```

La aplicación estará disponible en: `http://localhost:8080`

## 📡 API Endpoints

### Autenticación

#### Registro
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "fullName": "Juan Pérez"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "email": "user@example.com",
  "fullName": "Juan Pérez"
}
```

### Transacciones (Requiere autenticación)

#### Crear transacción
```http
POST /api/transactions
Authorization: Bearer {token}
Content-Type: application/json

{
  "amount": 1500.00,
  "currency": "MXN",
  "type": "INCOME",
  "category": "Salario",
  "description": "Pago mensual",
  "transactionDate": "2026-01-06"
}
```

#### Obtener todas las transacciones
```http
GET /api/transactions
Authorization: Bearer {token}
```

#### Obtener por tipo
```http
GET /api/transactions/type/EXPENSE
Authorization: Bearer {token}
```

#### Obtener por rango de fechas
```http
GET /api/transactions/date-range?startDate=2026-01-01&endDate=2026-01-31
Authorization: Bearer {token}
```

#### Obtener por categoría
```http
GET /api/transactions/category/Alimentación
Authorization: Bearer {token}
```

### Suscripciones (Requiere autenticación)

#### Crear suscripción
```http
POST /api/subscriptions
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Netflix",
  "description": "Plan Premium",
  "amount": 299.00,
  "currency": "MXN",
  "billingCycleDays": 30,
  "nextBillingDate": "2026-02-06"
}
```

#### Obtener suscripciones activas
```http
GET /api/subscriptions/active
Authorization: Bearer {token}
```

#### Pausar suscripción
```http
PUT /api/subscriptions/{id}/pause
Authorization: Bearer {token}
```

#### Cancelar suscripción
```http
PUT /api/subscriptions/{id}/cancel
Authorization: Bearer {token}
```

## 🎨 Patrones de Diseño Implementados

1. **Domain-Driven Design (DDD)**
   - Aggregate Roots
   - Value Objects
   - Domain Events
   - Specifications
   - Repository Pattern

2. **Hexagonal Architecture (Ports & Adapters)**
   - Domain como núcleo
   - Application como orquestador
   - Infrastructure como adaptadores

3. **Factory Method**
   - Creación controlada de entidades (`Transaction.create()`, `User.create()`)

4. **Specification Pattern**
   - Reglas de negocio composables y reutilizables

5. **Event-Driven**
   - Domain Events para comunicación entre agregados

## 📁 Estructura Completa del Proyecto

```
Microfinanzas/
├── src/
│   ├── main/
│   │   ├── java/com/silva/microfinanzas/
│   │   │   ├── domain/
│   │   │   │   ├── aggregates/
│   │   │   │   │   ├── Transaction.java (Aggregate Root)
│   │   │   │   │   └── TransactionItem.java
│   │   │   │   ├── entities/
│   │   │   │   │   ├── BaseEntity.java
│   │   │   │   │   ├── User.java
│   │   │   │   │   └── Subscription.java
│   │   │   │   ├── valueobjects/
│   │   │   │   │   ├── Money.java
│   │   │   │   │   ├── TransactionType.java
│   │   │   │   │   └── SubscriptionStatus.java
│   │   │   │   ├── events/
│   │   │   │   │   ├── DomainEvent.java
│   │   │   │   │   ├── DomainEventPublisher.java
│   │   │   │   │   ├── TransactionCreatedEvent.java
│   │   │   │   │   ├── UserRegisteredEvent.java
│   │   │   │   │   └── SubscriptionStatusChangedEvent.java
│   │   │   │   ├── repositories/
│   │   │   │   │   ├── TransactionRepository.java
│   │   │   │   │   ├── UserRepository.java
│   │   │   │   │   └── SubscriptionRepository.java
│   │   │   │   └── specifications/
│   │   │   │       ├── Specification.java
│   │   │   │       ├── TransactionExceedsAmountSpecification.java
│   │   │   │       ├── TransactionTypeSpecification.java
│   │   │   │       └── TransactionDateRangeSpecification.java
│   │   │   ├── application/
│   │   │   │   ├── dtos/
│   │   │   │   │   ├── CreateTransactionDTO.java
│   │   │   │   │   ├── TransactionResponseDTO.java
│   │   │   │   │   ├── RegisterUserDTO.java
│   │   │   │   │   ├── UserResponseDTO.java
│   │   │   │   │   ├── LoginRequestDTO.java
│   │   │   │   │   ├── JwtResponseDTO.java
│   │   │   │   │   ├── CreateSubscriptionDTO.java
│   │   │   │   │   └── SubscriptionResponseDTO.java
│   │   │   │   ├── mappers/
│   │   │   │   │   ├── TransactionMapper.java
│   │   │   │   │   ├── UserMapper.java
│   │   │   │   │   └── SubscriptionMapper.java
│   │   │   │   └── services/
│   │   │   │       ├── TransactionService.java
│   │   │   │       ├── UserService.java
│   │   │   │       └── SubscriptionService.java
│   │   │   ├── infrastructure/
│   │   │   │   ├── persistence/
│   │   │   │   │   ├── jpa/
│   │   │   │   │   │   ├── JpaTransactionRepository.java
│   │   │   │   │   │   ├── JpaUserRepository.java
│   │   │   │   │   │   └── JpaSubscriptionRepository.java
│   │   │   │   │   └── adapters/
│   │   │   │   │       ├── TransactionRepositoryAdapter.java
│   │   │   │   │       ├── UserRepositoryAdapter.java
│   │   │   │   │       └── SubscriptionRepositoryAdapter.java
│   │   │   │   ├── rest/
│   │   │   │   │   ├── AuthController.java
│   │   │   │   │   ├── TransactionController.java
│   │   │   │   │   ├── SubscriptionController.java
│   │   │   │   │   └── GlobalExceptionHandler.java
│   │   │   │   ├── security/
│   │   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │   │   └── SecurityConfig.java
│   │   │   │   └── config/
│   │   │   │       └── JpaConfig.java
│   │   │   └── MicrofinanzasApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/silva/microfinanzas/
│           └── MicrofinanzasApplicationTests.java
├── pom.xml
└── README.md
```

## 🧪 Testing

Ejecutar tests:
```bash
mvn test
```

## 📝 Notas Importantes

### Validaciones de Dominio
Las entidades de dominio contienen sus propias validaciones (invariantes), por ejemplo:
- `Transaction` valida que el monto sea positivo
- `User` valida el formato del email
- `Subscription` valida que el ciclo de facturación sea positivo

### Domain Events
Los eventos de dominio se publican automáticamente después de persistir:
```java
Transaction transaction = Transaction.create(...);
Transaction saved = transactionRepository.save(transaction);
saved.getDomainEvents().forEach(eventPublisher::publish);
saved.clearDomainEvents();
```

### Specifications
Ejemplo de uso de specifications combinadas:
```java
Specification<Transaction> spec = 
    new TransactionTypeSpecification(TransactionType.EXPENSE)
        .and(new TransactionExceedsAmountSpecification(Money.ofMXN(new BigDecimal("1000"))));

boolean matches = spec.isSatisfiedBy(transaction);
```

## 🔮 Mejoras Futuras

- [ ] Implementar CQRS (Command Query Responsibility Segregation)
- [ ] Agregar Swagger/OpenAPI para documentación de API
- [ ] Implementar Event Sourcing
- [ ] Agregar tests unitarios e integración
- [ ] Implementar cache con Redis
- [ ] Agregar métricas con Actuator
- [ ] Dockerizar la aplicación

## 👨‍💻 Autor

**Silva Industries**

## 📄 Licencia

Este proyecto es privado y confidencial.

