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

### Queues (authenticated)

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/queues` | List all (optional `?organizationId=1`) |
| GET | `/api/v1/queues/{id}` | Get by id |
| POST | `/api/v1/queues` | Create |
| PUT | `/api/v1/queues/{id}` | Update |
| DELETE | `/api/v1/queues/{id}` | Delete |

Create/update body:

```json
{
  "name": "Front Desk",
  "organizationId": 1,
  "status": "ACTIVE"
}
```

Response fields: `id`, `name`, `organizationId`, `status`, `createdAt`

`status`: `ACTIVE`, `PAUSED`, `CLOSED` (defaults to `ACTIVE` on create)

## Test

```bash
mvn test
```
