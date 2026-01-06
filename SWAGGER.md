# 📘 Guía de Uso de Swagger - Microfinanzas API

## 🌐 Acceder a Swagger UI

Una vez que la aplicación esté ejecutándose, accede a Swagger UI en tu navegador:

```
http://localhost:8080/swagger-ui.html
```

O también puedes usar:
```
http://localhost:8080/swagger-ui/index.html
```

## 📋 URLs Importantes

- **Swagger UI (Interfaz Interactiva):** http://localhost:8080/swagger-ui.html
- **API Docs (JSON):** http://localhost:8080/api-docs
- **API Docs (YAML):** http://localhost:8080/api-docs.yaml

## 🔐 Cómo Autenticarte en Swagger

### Paso 1: Registrar un Usuario
1. En Swagger UI, busca la sección **"Autenticación"**
2. Expande el endpoint `POST /api/auth/register`
3. Haz clic en **"Try it out"**
4. Modifica el JSON de ejemplo:
   ```json
   {
     "email": "miusuario@example.com",
     "password": "password123",
     "fullName": "Mi Nombre Completo"
   }
   ```
5. Haz clic en **"Execute"**
6. Deberías recibir una respuesta `201 Created` con los datos del usuario

### Paso 2: Iniciar Sesión
1. Expande el endpoint `POST /api/auth/login`
2. Haz clic en **"Try it out"**
3. Ingresa tus credenciales:
   ```json
   {
     "email": "miusuario@example.com",
     "password": "password123"
   }
   ```
4. Haz clic en **"Execute"**
5. En la respuesta, **copia el token JWT** (el valor del campo `token`)

### Paso 3: Autorizar en Swagger
1. En la parte superior derecha de Swagger UI, haz clic en el botón **"Authorize"** 🔒
2. En el campo que aparece, pega tu token en el formato:
   ```
   Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```
   ⚠️ **Importante:** Debe incluir la palabra `Bearer` seguida de un espacio y luego el token
3. Haz clic en **"Authorize"**
4. Haz clic en **"Close"**

**¡Listo!** Ahora puedes probar todos los endpoints protegidos.

## 📝 Ejemplos de Uso

### 1. Crear una Transacción (Gasto)
```json
{
  "userId": 1,
  "description": "Compra en supermercado",
  "amount": 150.50,
  "currency": "USD",
  "type": "EXPENSE",
  "category": "Alimentación",
  "transactionDate": "2026-01-06"
}
```

### 2. Crear una Transacción (Ingreso)
```json
{
  "userId": 1,
  "description": "Salario mensual",
  "amount": 2500.00,
  "currency": "USD",
  "type": "INCOME",
  "category": "Salario",
  "transactionDate": "2026-01-01"
}
```

### 3. Crear una Suscripción
```json
{
  "userId": 1,
  "name": "Netflix",
  "description": "Suscripción mensual a Netflix",
  "amount": 15.99,
  "currency": "USD",
  "frequency": "MONTHLY",
  "startDate": "2026-01-01",
  "nextBillingDate": "2026-02-01"
}
```

## 🎯 Características de Swagger UI

### ✅ Funcionalidades Disponibles

1. **Explorar Endpoints:** Ver todos los endpoints organizados por categorías
2. **Probar APIs:** Ejecutar requests directamente desde el navegador
3. **Ver Esquemas:** Visualizar la estructura de DTOs y respuestas
4. **Ver Validaciones:** Ver las validaciones requeridas para cada campo
5. **Ver Respuestas:** Ver ejemplos de respuestas exitosas y errores
6. **Descargar Especificación:** Exportar la documentación en JSON/YAML

### 📂 Secciones Principales

- **Autenticación:** Endpoints públicos (registro, login)
- **Usuarios:** Gestión de perfiles de usuario
- **Transacciones:** Registro de ingresos y gastos
- **Suscripciones:** Gestión de suscripciones recurrentes

### 🔍 Filtros y Búsqueda

- **Buscar endpoints:** Usa el campo de búsqueda en la parte superior
- **Filtrar por tag:** Haz clic en las secciones para expandir/contraer
- **Ordenar:** Los endpoints están ordenados alfabéticamente

## 🎨 Tipos de Transacción

| Tipo | Descripción |
|------|-------------|
| `INCOME` | Ingresos (salario, bonos, etc.) |
| `EXPENSE` | Gastos (compras, servicios, etc.) |

## 🔄 Frecuencias de Suscripción

| Frecuencia | Descripción |
|------------|-------------|
| `DAILY` | Diaria |
| `WEEKLY` | Semanal |
| `MONTHLY` | Mensual |
| `YEARLY` | Anual |

## 📊 Estados de Suscripción

| Estado | Descripción |
|--------|-------------|
| `ACTIVE` | Suscripción activa |
| `PAUSED` | Suscripción pausada |
| `CANCELLED` | Suscripción cancelada |

## 🛠️ Códigos de Respuesta HTTP

| Código | Significado |
|--------|-------------|
| **200** | OK - Operación exitosa |
| **201** | Created - Recurso creado |
| **400** | Bad Request - Datos inválidos |
| **401** | Unauthorized - No autenticado |
| **403** | Forbidden - Sin permisos |
| **404** | Not Found - Recurso no encontrado |
| **409** | Conflict - Conflicto (ej: email duplicado) |
| **500** | Internal Server Error - Error del servidor |

## 💡 Consejos y Trucos

### 1. Guardar Colecciones
Puedes exportar la especificación OpenAPI y usarla en otras herramientas:
- Postman
- Insomnia
- HTTPie

### 2. Probar Diferentes Escenarios
Swagger UI te permite probar fácilmente:
- ✅ Casos exitosos
- ❌ Validaciones de errores
- 🔒 Endpoints protegidos
- 📝 Diferentes tipos de datos

### 3. Ver Modelos de Datos
En la parte inferior de Swagger UI, hay una sección **"Schemas"** donde puedes ver todos los modelos de datos.

### 4. Copiar cURL
Cada request en Swagger incluye un comando cURL que puedes copiar y usar en terminal:
```bash
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}'
```

## 🔧 Personalización

### Cambiar el Puerto
Si cambias el puerto en `application.properties`:
```properties
server.port=8081
```

Swagger estará disponible en:
```
http://localhost:8081/swagger-ui.html
```

### Deshabilitar Swagger en Producción
En `application.properties` (para producción):
```properties
springdoc.swagger-ui.enabled=false
springdoc.api-docs.enabled=false
```

## 🎓 Recursos Adicionales

- **SpringDoc OpenAPI:** https://springdoc.org/
- **OpenAPI Specification:** https://swagger.io/specification/
- **Swagger Editor:** https://editor.swagger.io/

## 🐛 Troubleshooting

### Swagger no carga
1. Verifica que la aplicación esté ejecutándose
2. Verifica la URL (debe incluir `/swagger-ui.html`)
3. Revisa los logs de la aplicación

### Error 403 en endpoints protegidos
1. Asegúrate de haber iniciado sesión
2. Verifica que hayas autorizado con el token correcto
3. Verifica que el token no haya expirado (24 horas)

### El token expira
Los tokens JWT expiran después de 24 horas. Simplemente:
1. Vuelve a hacer login
2. Obtén un nuevo token
3. Vuelve a autorizar en Swagger

---

## 🎉 ¡Disfruta probando tu API!

Swagger UI hace que probar y documentar tu API sea muy fácil y visual. Experimenta con diferentes endpoints y casos de uso.

**¿Preguntas?** Revisa la documentación de SpringDoc OpenAPI o los comentarios en el código fuente.

