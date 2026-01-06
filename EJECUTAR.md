# 🚀 Guía de Ejecución - Microservicio de Microfinanzas

## 📋 Prerequisitos

Antes de ejecutar la aplicación, asegúrate de tener instalado:

1. **Java 17 o superior**
   ```powershell
   java -version
   ```

2. **Maven 3.6+**
   ```powershell
   mvn -version
   ```

3. **MySQL 8.0+**
   - El servidor MySQL debe estar ejecutándose
   - Puerto por defecto: 3306
   - Usuario: root
   - Contraseña: root (o modificar en `application.properties`)

## 🗄️ Configuración de Base de Datos

### Opción 1: Creación Automática (Recomendado)
La aplicación creará automáticamente la base de datos `microfinanzas` si no existe.

### Opción 2: Creación Manual
Si prefieres crear la base de datos manualmente:

```sql
CREATE DATABASE IF NOT EXISTS microfinanzas;
USE microfinanzas;
```

**Nota:** No necesitas crear las tablas, Hibernate las creará automáticamente.

## ▶️ Cómo Ejecutar

### Método 1: Script PowerShell (Recomendado)
```powershell
.\run.ps1
```

### Método 2: Maven directamente
```powershell
mvn spring-boot:run
```

### Método 3: Compilar y ejecutar JAR
```powershell
mvn clean package
java -jar target/Microfinanzas-0.0.1-SNAPSHOT.jar
```

## ✅ Verificar que la Aplicación Está Corriendo

Una vez iniciada, deberías ver:
```
Started MicrofinanzasApplication in X seconds
```

La aplicación estará disponible en:
- **URL Base:** http://localhost:8080
- **Puerto:** 8080

## 🔑 Endpoints Disponibles

### Autenticación (Públicos)
- `POST /api/auth/register` - Registrar nuevo usuario
- `POST /api/auth/login` - Iniciar sesión

### Usuarios (Requiere autenticación)
- `GET /api/users/me` - Obtener perfil del usuario actual
- `GET /api/users/{id}` - Obtener usuario por ID

### Transacciones (Requiere autenticación)
- `POST /api/transactions` - Crear nueva transacción
- `GET /api/transactions/user/{userId}` - Listar transacciones del usuario
- `GET /api/transactions/{id}` - Obtener transacción por ID

### Suscripciones (Requiere autenticación)
- `POST /api/subscriptions` - Crear nueva suscripción
- `GET /api/subscriptions/user/{userId}` - Listar suscripciones del usuario
- `GET /api/subscriptions/{id}` - Obtener suscripción por ID
- `PUT /api/subscriptions/{id}/status` - Actualizar estado de suscripción

## 📝 Ejemplo de Uso

### 1. Registrar un Usuario
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" `
  -Method POST `
  -ContentType "application/json" `
  -Body '{
    "email": "usuario@example.com",
    "password": "password123",
    "firstName": "Juan",
    "lastName": "Pérez"
  }'
```

### 2. Iniciar Sesión
```powershell
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
  -Method POST `
  -ContentType "application/json" `
  -Body '{
    "email": "usuario@example.com",
    "password": "password123"
  }'

$token = $response.token
```

### 3. Crear una Transacción
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/transactions" `
  -Method POST `
  -ContentType "application/json" `
  -Headers @{ "Authorization" = "Bearer $token" } `
  -Body '{
    "userId": 1,
    "description": "Compra de supermercado",
    "amount": 150.50,
    "currency": "USD",
    "type": "EXPENSE",
    "category": "Alimentación",
    "transactionDate": "2026-01-06"
  }'
```

## 🛠️ Solución de Problemas

### Error: "Public Key Retrieval is not allowed"
**Solución:** Ya está configurado `allowPublicKeyRetrieval=true` en `application.properties`

### Error: "Access denied for user 'root'@'localhost'"
**Solución:** Verifica usuario y contraseña en `src/main/resources/application.properties`

### Error: "Communications link failure"
**Solución:** 
1. Verifica que MySQL esté ejecutándose
2. Verifica que el puerto sea 3306
3. Verifica que MySQL acepte conexiones en localhost

### Puerto 8080 ya está en uso
**Solución:** Cambia el puerto en `application.properties`:
```properties
server.port=8081
```

## 📊 Ver Logs

Los logs se mostrarán en la consola con nivel DEBUG para:
- Queries SQL de Hibernate
- Operaciones de la aplicación

## 🔒 Seguridad

- Las contraseñas se encriptan con BCrypt
- JWT con expiración de 24 horas
- Tokens de refresh con expiración de 7 días
- Endpoints protegidos con Spring Security

## 🏗️ Arquitectura

El proyecto sigue el patrón **Domain-Driven Design (DDD)**:
- **Domain:** Entidades, Value Objects, Repositorios (interfaces)
- **Application:** Servicios, DTOs, Mappers
- **Infrastructure:** Implementaciones, Controladores REST, Seguridad

## 📚 Tecnologías Utilizadas

- Java 17
- Spring Boot 3.4.1
- Spring Security + JWT
- Spring Data JPA
- Hibernate
- MySQL 8+
- Lombok
- MapStruct
- Maven

---

**¿Necesitas ayuda?** Revisa los logs en la consola para identificar errores específicos.

