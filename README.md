# VetCare — Sistema de Gestión Veterinaria

## Contexto del proyecto

VetCare es un sistema de gestión para clínicas veterinarias, desarrollado bajo una arquitectura de microservicios con Spring Boot. Permite administrar clientes (dueños de mascotas), mascotas, veterinarios, atenciones clínicas y vacunas, centralizando el acceso a través de un API Gateway con autenticación JWT. Un servicio orquestador consolida la información de múltiples microservicios en una ficha médica integral por cliente.

Proyecto desarrollado para la asignatura **DSY1103 — Desarrollo FullStack 1**, Evaluación Parcial 3, Duoc UC.

## Integrantes del equipo

- Paloma Fuentes
- Felipe Duran
- Victor Salas

## Microservicios implementados

| Microservicio | Puerto | Descripción |
|---|---|---|
| `api-gateway` | 8080 | Punto de entrada único. Enruta solicitudes y valida JWT mediante `AuthenticationFilter` |
| `auth` | 8084 | Registro de usuarios, login y generación de tokens JWT |
| `cliente` | 8081 | CRUD de clientes (dueños de mascotas) |
| `mascota` | 8082 | CRUD de mascotas, con borrado lógico (`activo`) |
| `veterinario` | 8083 | CRUD del personal médico de la clínica |
| `atencion` | 8085 | Registro de consultas y tratamientos clínicos |
| `vacuna` | 8086 | Registro y seguimiento del historial de vacunación |
| `orquestador` | 8088 | Consolida datos de Cliente, Mascota, Atención, Vacuna y Veterinario en una ficha médica integral, vía WebClient |

## Rutas principales del API Gateway

Todas las rutas pasan por `http://localhost:8080` y requieren JWT (excepto `/api/v1/auth/**`):

| Ruta | Microservicio destino |
|---|---|
| `POST /api/v1/auth/register` | auth (público) |
| `POST /api/v1/auth/login` | auth (público) |
| `/api/v1/clientes/**` | cliente |
| `/api/v1/mascotas/**` | mascota |
| `/api/v1/veterinarios/**` | veterinario |
| `/api/v1/atenciones/**` | atencion |
| `/api/v1/vacunas/**` | vacuna |
| `/api/v1/consultas/**` | orquestador |

## Documentación Swagger / OpenAPI

| Microservicio | URL Swagger |
|---|---|
| API Gateway (agregado) | http://localhost:8080/swagger-ui.html |
| Auth | http://localhost:8084/swagger-ui.html |
| Cliente | http://localhost:8081/swagger-ui.html |
| Mascota | http://localhost:8082/swagger-ui.html |
| Veterinario | http://localhost:8083/swagger-ui.html |
| Atención | http://localhost:8085/swagger-ui.html |
| Vacuna | http://localhost:8086/swagger-ui.html |
| Orquestador | http://localhost:8088/swagger-ui.html |

## Pruebas unitarias

6 pruebas unitarias con JUnit 5 + Mockito en `mascota/src/test/java/com/duoc/mascota/MascotaServiceTest.java`, cubriendo las reglas de negocio de `MascotaService`: registro, búsqueda activa, listado, listado por cliente, actualización y borrado lógico.

```bash
cd mascota
./mvnw test
```

## Ejecución local

### Requisitos
- Docker Desktop instalado y en ejecución
- Puertos libres: 8080–8086, 8088, 3307

### Levantar el sistema completo

```bash
git clone https://github.com/paloma-fm/vetcare-microservices.git
cd vetcare-microservices
docker-compose up --build
```

Verificar que los 9 contenedores estén activos:
```bash
docker-compose ps
```

### Flujo de prueba

1. Registrar usuario: `POST http://localhost:8084/api/v1/auth/register`
   ```json
   { "username": "admin", "password": "123456", "role": "USER" }
   ```
2. Iniciar sesión: `POST http://localhost:8084/api/v1/auth/login` → obtener token JWT
3. Usar el token en el header `Authorization: Bearer <token>` para los endpoints protegidos vía Gateway (`http://localhost:8080`)
4. Crear cliente, mascota, veterinario, atención y vacuna usando los Swagger de cada servicio
5. Consultar la ficha médica integral: `GET http://localhost:8088/api/v1/consultas/ficha-completa/{clienteId}`

### Verificar base de datos

```bash
docker exec -it vetcare-mysql mysql -uroot -proot1234
SHOW DATABASES;
USE vetcare_mascotas;
SELECT * FROM mascota;
```

## Detener el sistema

```bash
docker-compose down       # detiene contenedores, conserva datos
docker-compose down -v    # detiene contenedores y borra datos
```

## Stack tecnológico

- Java 21 (microservicios) / Java 17 (api-gateway)
- Spring Boot 3.4.0 / Spring Cloud Gateway 3.3.2
- Spring Data JPA + MySQL 8.0
- JWT para autenticación
- JUnit 5 + Mockito
- SpringDoc OpenAPI (Swagger UI)
- Docker + Docker Compose
