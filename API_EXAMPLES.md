# Ejemplos de Uso de la API - Microfinanzas

Esta guía contiene ejemplos prácticos de cómo usar la API del microservicio de Microfinanzas.

## 📋 Tabla de Contenidos
1. [Autenticación](#autenticación)
2. [Gestión de Transacciones](#gestión-de-transacciones)
3. [Gestión de Suscripciones](#gestión-de-suscripciones)
4. [Ejemplos con cURL](#ejemplos-con-curl)
5. [Colección de Postman](#colección-de-postman)

## Autenticación

### 1. Registro de Usuario

**Endpoint:** `POST /api/auth/register`

**Request Body:**
```json
{
  "email": "juan.perez@example.com",
  "password": "securePassword123",
  "fullName": "Juan Pérez García"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "email": "juan.perez@example.com",
  "fullName": "Juan Pérez García",
  "enabled": true,
  "roles": ["ROLE_USER"],
  "createdAt": "2026-01-06T10:30:00"
}
```

### 2. Login

**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "email": "juan.perez@example.com",
  "password": "securePassword123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFuLnBlcmV6QGV4YW1wbGUuY29tIiwiaWF0IjoxNzA0NTQ2MDAwLCJleHAiOjE3MDQ2MzI0MDB9.abc123...",
  "type": "Bearer",
  "userId": 1,
  "email": "juan.perez@example.com",
  "fullName": "Juan Pérez García"
}
```

**⚠️ Importante:** Guarda el token JWT, lo necesitarás para todas las siguientes peticiones.

---

## Gestión de Transacciones

**Todas las peticiones requieren el header:**
```
Authorization: Bearer {tu-token-jwt}
```

### 1. Crear Ingreso

**Endpoint:** `POST /api/transactions`

**Request Body:**
```json
{
  "amount": 15000.00,
  "currency": "MXN",
  "type": "INCOME",
  "category": "Salario",
  "description": "Pago de nómina mensual",
  "transactionDate": "2026-01-05"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "amount": 15000.00,
  "currency": "MXN",
  "type": "INCOME",
  "category": "Salario",
  "description": "Pago de nómina mensual",
  "transactionDate": "2026-01-05",
  "createdAt": "2026-01-06T10:35:00",
  "updatedAt": "2026-01-06T10:35:00"
}
```

### 2. Crear Gasto

**Endpoint:** `POST /api/transactions`

**Request Body:**
```json
{
  "amount": 850.50,
  "currency": "MXN",
  "type": "EXPENSE",
  "category": "Alimentación",
  "description": "Compras del supermercado",
  "transactionDate": "2026-01-06"
}
```

### 3. Obtener Todas las Transacciones

**Endpoint:** `GET /api/transactions`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "amount": 15000.00,
    "currency": "MXN",
    "type": "INCOME",
    "category": "Salario",
    "description": "Pago de nómina mensual",
    "transactionDate": "2026-01-05",
    "createdAt": "2026-01-06T10:35:00",
    "updatedAt": "2026-01-06T10:35:00"
  },
  {
    "id": 2,
    "amount": 850.50,
    "currency": "MXN",
    "type": "EXPENSE",
    "category": "Alimentación",
    "description": "Compras del supermercado",
    "transactionDate": "2026-01-06",
    "createdAt": "2026-01-06T11:00:00",
    "updatedAt": "2026-01-06T11:00:00"
  }
]
```

### 4. Obtener Transacción por ID

**Endpoint:** `GET /api/transactions/1`

**Response (200 OK):**
```json
{
  "id": 1,
  "amount": 15000.00,
  "currency": "MXN",
  "type": "INCOME",
  "category": "Salario",
  "description": "Pago de nómina mensual",
  "transactionDate": "2026-01-05",
  "createdAt": "2026-01-06T10:35:00",
  "updatedAt": "2026-01-06T10:35:00"
}
```

### 5. Filtrar por Tipo (Solo Gastos)

**Endpoint:** `GET /api/transactions/type/EXPENSE`

**Response (200 OK):**
```json
[
  {
    "id": 2,
    "amount": 850.50,
    "currency": "MXN",
    "type": "EXPENSE",
    "category": "Alimentación",
    "description": "Compras del supermercado",
    "transactionDate": "2026-01-06",
    "createdAt": "2026-01-06T11:00:00",
    "updatedAt": "2026-01-06T11:00:00"
  }
]
```

### 6. Filtrar por Tipo (Solo Ingresos)

**Endpoint:** `GET /api/transactions/type/INCOME`

### 7. Filtrar por Rango de Fechas

**Endpoint:** `GET /api/transactions/date-range?startDate=2026-01-01&endDate=2026-01-31`

### 8. Filtrar por Categoría

**Endpoint:** `GET /api/transactions/category/Alimentación`

### 9. Actualizar Transacción

**Endpoint:** `PUT /api/transactions/2`

**Request Body:**
```json
{
  "amount": 900.00,
  "currency": "MXN",
  "type": "EXPENSE",
  "category": "Alimentación",
  "description": "Compras del supermercado - Actualizado",
  "transactionDate": "2026-01-06"
}
```

### 10. Eliminar Transacción

**Endpoint:** `DELETE /api/transactions/2`

**Response (204 No Content)**

---

## Gestión de Suscripciones

### 1. Crear Suscripción

**Endpoint:** `POST /api/subscriptions`

**Request Body:**
```json
{
  "name": "Netflix Premium",
  "description": "Plan familiar 4 pantallas",
  "amount": 299.00,
  "currency": "MXN",
  "billingCycleDays": 30,
  "nextBillingDate": "2026-02-06"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Netflix Premium",
  "description": "Plan familiar 4 pantallas",
  "amount": 299.00,
  "currency": "MXN",
  "billingCycleDays": 30,
  "nextBillingDate": "2026-02-06",
  "status": "ACTIVE",
  "createdAt": "2026-01-06T12:00:00",
  "updatedAt": "2026-01-06T12:00:00"
}
```

### 2. Crear Más Suscripciones (Ejemplos)

**Spotify:**
```json
{
  "name": "Spotify Premium",
  "description": "Plan individual",
  "amount": 119.00,
  "currency": "MXN",
  "billingCycleDays": 30,
  "nextBillingDate": "2026-01-15"
}
```

**Adobe Creative Cloud:**
```json
{
  "name": "Adobe Creative Cloud",
  "description": "Todas las apps",
  "amount": 1299.00,
  "currency": "MXN",
  "billingCycleDays": 30,
  "nextBillingDate": "2026-01-20"
}
```

**Amazon Prime (Anual):**
```json
{
  "name": "Amazon Prime",
  "description": "Membresía anual",
  "amount": 899.00,
  "currency": "MXN",
  "billingCycleDays": 365,
  "nextBillingDate": "2027-01-06"
}
```

### 3. Obtener Todas las Suscripciones

**Endpoint:** `GET /api/subscriptions`

### 4. Obtener Solo Suscripciones Activas

**Endpoint:** `GET /api/subscriptions/active`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Netflix Premium",
    "description": "Plan familiar 4 pantallas",
    "amount": 299.00,
    "currency": "MXN",
    "billingCycleDays": 30,
    "nextBillingDate": "2026-02-06",
    "status": "ACTIVE",
    "createdAt": "2026-01-06T12:00:00",
    "updatedAt": "2026-01-06T12:00:00"
  },
  {
    "id": 2,
    "name": "Spotify Premium",
    "description": "Plan individual",
    "amount": 119.00,
    "currency": "MXN",
    "billingCycleDays": 30,
    "nextBillingDate": "2026-01-15",
    "status": "ACTIVE",
    "createdAt": "2026-01-06T12:05:00",
    "updatedAt": "2026-01-06T12:05:00"
  }
]
```

### 5. Pausar Suscripción

**Endpoint:** `PUT /api/subscriptions/1/pause`

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Netflix Premium",
  "description": "Plan familiar 4 pantallas",
  "amount": 299.00,
  "currency": "MXN",
  "billingCycleDays": 30,
  "nextBillingDate": "2026-02-06",
  "status": "PAUSED",
  "createdAt": "2026-01-06T12:00:00",
  "updatedAt": "2026-01-06T13:00:00"
}
```

### 6. Reactivar Suscripción

**Endpoint:** `PUT /api/subscriptions/1/reactivate`

### 7. Cancelar Suscripción

**Endpoint:** `PUT /api/subscriptions/1/cancel`

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Netflix Premium",
  "description": "Plan familiar 4 pantallas",
  "amount": 299.00,
  "currency": "MXN",
  "billingCycleDays": 30,
  "nextBillingDate": "2026-02-06",
  "status": "CANCELLED",
  "createdAt": "2026-01-06T12:00:00",
  "updatedAt": "2026-01-06T14:00:00"
}
```

### 8. Eliminar Suscripción

**Endpoint:** `DELETE /api/subscriptions/1`

**Response (204 No Content)**

---

## Ejemplos con cURL

### Registro
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "juan.perez@example.com",
    "password": "securePassword123",
    "fullName": "Juan Pérez García"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "juan.perez@example.com",
    "password": "securePassword123"
  }'
```

### Crear Transacción (reemplaza TOKEN con tu JWT)
```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{
    "amount": 15000.00,
    "currency": "MXN",
    "type": "INCOME",
    "category": "Salario",
    "description": "Pago de nómina mensual",
    "transactionDate": "2026-01-05"
  }'
```

### Obtener Transacciones
```bash
curl -X GET http://localhost:8080/api/transactions \
  -H "Authorization: Bearer TOKEN"
```

### Crear Suscripción
```bash
curl -X POST http://localhost:8080/api/subscriptions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{
    "name": "Netflix Premium",
    "description": "Plan familiar 4 pantallas",
    "amount": 299.00,
    "currency": "MXN",
    "billingCycleDays": 30,
    "nextBillingDate": "2026-02-06"
  }'
```

---

## Colección de Postman

### Variables de Entorno

Crea un environment en Postman con estas variables:

```json
{
  "base_url": "http://localhost:8080",
  "token": "",
  "user_id": ""
}
```

### Scripts de Postman

**Para el endpoint de Login, agrega este script en "Tests":**
```javascript
// Guardar el token automáticamente
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("token", jsonData.token);
    pm.environment.set("user_id", jsonData.userId);
}
```

**En todas las otras peticiones, usa:**
- URL: `{{base_url}}/api/...`
- Header: `Authorization` con valor `Bearer {{token}}`

---

## Códigos de Estado HTTP

| Código | Significado |
|--------|-------------|
| 200 | OK - Petición exitosa |
| 201 | Created - Recurso creado exitosamente |
| 204 | No Content - Recurso eliminado exitosamente |
| 400 | Bad Request - Error en los datos enviados |
| 401 | Unauthorized - Token inválido o faltante |
| 403 | Forbidden - Sin permisos |
| 404 | Not Found - Recurso no encontrado |
| 409 | Conflict - Conflicto (ej: email ya existe) |
| 500 | Internal Server Error - Error del servidor |

---

## Casos de Uso Completos

### Escenario 1: Usuario Nuevo Gestiona sus Finanzas

```bash
# 1. Registro
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"maria@example.com","password":"pass123","fullName":"María López"}'

# 2. Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"maria@example.com","password":"pass123"}'

# 3. Registrar ingreso
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{"amount":10000,"currency":"MXN","type":"INCOME","category":"Salario","description":"Quincena","transactionDate":"2026-01-06"}'

# 4. Registrar gastos
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{"amount":500,"currency":"MXN","type":"EXPENSE","category":"Transporte","description":"Gasolina","transactionDate":"2026-01-06"}'

# 5. Ver resumen del mes
curl -X GET "http://localhost:8080/api/transactions/date-range?startDate=2026-01-01&endDate=2026-01-31" \
  -H "Authorization: Bearer TOKEN"
```

### Escenario 2: Gestión de Suscripciones

```bash
# 1. Agregar suscripciones
curl -X POST http://localhost:8080/api/subscriptions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{"name":"Netflix","amount":299,"currency":"MXN","billingCycleDays":30,"nextBillingDate":"2026-02-01"}'

# 2. Ver suscripciones activas
curl -X GET http://localhost:8080/api/subscriptions/active \
  -H "Authorization: Bearer TOKEN"

# 3. Pausar temporalmente
curl -X PUT http://localhost:8080/api/subscriptions/1/pause \
  -H "Authorization: Bearer TOKEN"

# 4. Reactivar
curl -X PUT http://localhost:8080/api/subscriptions/1/reactivate \
  -H "Authorization: Bearer TOKEN"
```

---

## Consejos y Buenas Prácticas

1. **Siempre incluye el token JWT** en el header `Authorization: Bearer {token}`

2. **Valida las fechas**: `transactionDate` no puede ser futura, `nextBillingDate` debe ser futura

3. **Usa las monedas correctas**: El código de moneda debe ser de 3 caracteres (MXN, USD, EUR)

4. **Maneja los errores**: Verifica los mensajes de error en el response body

5. **Renueva el token**: Los tokens expiran (por defecto 24 horas), haz login nuevamente

6. **Usa categorías consistentes**: Mantén un listado de categorías para mejor organización

---

## Testing Automatizado

### Ejemplo con Jest (JavaScript/Node.js)

```javascript
const axios = require('axios');

const BASE_URL = 'http://localhost:8080/api';
let token = '';

async function testFlow() {
  // 1. Register
  const registerRes = await axios.post(`${BASE_URL}/auth/register`, {
    email: 'test@example.com',
    password: 'test123',
    fullName: 'Test User'
  });
  
  // 2. Login
  const loginRes = await axios.post(`${BASE_URL}/auth/login`, {
    email: 'test@example.com',
    password: 'test123'
  });
  token = loginRes.data.token;
  
  // 3. Create transaction
  const txRes = await axios.post(`${BASE_URL}/transactions`, {
    amount: 1000,
    currency: 'MXN',
    type: 'INCOME',
    category: 'Test',
    description: 'Test transaction',
    transactionDate: '2026-01-06'
  }, {
    headers: { Authorization: `Bearer ${token}` }
  });
  
  console.log('Transaction created:', txRes.data);
}

testFlow();
```

---

¡Feliz testing! 🚀

