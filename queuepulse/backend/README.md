# QueuePulse Backend

Spring Boot 3 API (Java 21).

## Stack

- Spring Web
- Spring Data JPA
- Spring Security
- MySQL
- Lombok
- Kafka (producer)

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

## Kafka

When a user joins a queue, a `QueueJoinedEvent` is published to topic **`queue.joined`** (after DB commit).

A **Kafka consumer** (`queuepulse-analytics` group) consumes the event and stores rows in `queue_join_analytics` for peak-hour traffic metrics on the dashboard.

```bash
# Local Kafka (example)
docker run -d --name kafka -p 9092:9092 apache/kafka:latest
```

Set `KAFKA_BOOTSTRAP_SERVERS` if not using `localhost:9092`.

Event payload:

```json
{
  "entryId": 1,
  "queueId": 1,
  "organizationId": 1,
  "token": "A101",
  "joinedAt": "2026-05-31T10:00:00Z",
  "position": 1
}
```

## Docker

```bash
docker build -t queuepulse-backend .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/queuepulse \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=root \
  queuepulse-backend
```

Port **8080** is exposed. Override datasource and `JWT_SECRET` via environment variables as needed.

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

### Join queue

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/queues/{id}/join` | Issue next token (`A101`, `A102`, …) |

Response:

```json
{
  "id": 1,
  "queueId": 1,
  "organizationId": 1,
  "token": "A101",
  "joinedAt": "2026-05-31T10:00:00Z",
  "position": 1
}
```

Tokens use the queue prefix (default `A`) starting at `101`. Only `ACTIVE` queues accept joins.

Mark a customer as served (sets `servedAt` for analytics):

```
POST /api/v1/queues/entries/{entryId}/serve
```

### Analytics (authenticated)

```
GET /api/v1/analytics?queueId=1&organizationId=1
```

| Metric | Description |
|--------|-------------|
| `averageWaitingTimeSeconds` | Avg `servedAt - joinedAt` for served entries (JPQL) |
| `customersServedToday` | Count served today (UTC) |
| `peakHour` / `peakHourTraffic` | Busiest join hour today (0–23) |

Optional filters: `queueId`, `organizationId`

## Test

```bash
mvn test
```
