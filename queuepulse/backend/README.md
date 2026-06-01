# QueuePulse Backend

Spring Boot 3 API (Java 21).

## Stack

- Spring Web
- Spring Data JPA
- Spring Security
- MySQL
- Lombok

## Layout

```
src/main/java/com/queuepulse/
├── QueuePulseApplication.java
├── config/       # Security, JPA
├── controller/   # REST endpoints
├── service/      # Business logic
├── repository/   # Data access
├── entity/       # JPA models
└── dto/          # API request/response types
```

## Run

1. Start MySQL and create database `queuepulse` (or rely on `createDatabaseIfNotExist=true`).
2. Set credentials in `src/main/resources/application.yml`.
3. From this folder:

```bash
mvn spring-boot:run
```

### Auth (public)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/auth/register` | Create account (default role: `CUSTOMER`) |
| POST | `/auth/login` | Returns JWT |

Register body:

```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "password123",
  "role": "CUSTOMER"
}
```

Login body:

```json
{
  "email": "jane@example.com",
  "password": "password123"
}
```

Use the token on protected routes:

```
Authorization: Bearer <token>
```

API base: `http://localhost:8080/api/v1/queues` (requires authentication)

## Test

```bash
mvn test
```
