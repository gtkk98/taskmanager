# Task Manager API

A RESTful Task Management API built with **Java**, **Spring Boot**, and **PostgreSQL**. Supports user management, task creation, assignment, status tracking, and paginated queries.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21+ |
| Framework | Spring Boot 4.x |
| ORM | Spring Data JPA + Hibernate 7 |
| Database | PostgreSQL 18 |
| Build Tool | Maven |
| Utilities | Lombok, Spring DevTools |

---

## Project Structure
```
src/main/java/com/thilina/taskmanager/
├── controller/
│   ├── TaskController.java       # REST endpoints for tasks
│   └── UserController.java       # REST endpoints for users
├── service/
│   ├── TaskService.java          # Business logic for tasks
│   └── UserService.java          # Business logic for users
├── repository/
│   ├── TaskRepository.java       # JPA queries for tasks
│   └── UserRepository.java       # JPA queries for users
├── model/
│   ├── Task.java                 # Task entity + Status enum
│   └── User.java                 # User entity + Role enum
├── dto/
│   ├── TaskRequest.java          # Incoming task payload
│   ├── TaskResponse.java         # Outgoing task payload
│   └── UserRequest.java          # Incoming user payload
└── exception/
    └── GlobalExceptionHandler.java
```

---

## Architecture
```
HTTP Client
    │
    ▼
┌─────────────────────────────────────────────────────────┐
│  Controller  (@RestController)                          │
│  Receives HTTP requests, returns JSON responses         │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────┐
│  Service  (@Service)                                    │
│  Business logic, validation, orchestration              │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────┐
│  Repository  (@Repository)                              │
│  Database queries via Spring Data JPA                   │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
               [ PostgreSQL DB ]
```

---

## Request Flow
```
POST /api/tasks  {"title": "Build API", "assignedToId": 1}
        │
        ▼
TaskController.createTask()
  → validates JSON body (@RequestBody)
  → builds Task object from TaskRequest DTO
        │
        ▼
TaskService.createTask()
  → checks if assignedTo user exists
  → sets default status to TODO
  → calls repository.save()
        │
        ▼
TaskRepository (JpaRepository)
  → Hibernate generates INSERT SQL
  → executes against PostgreSQL
        │
        ▼
Returns TaskResponse DTO  →  201 Created
```

---

## Database Schema

**users**

| Column | Type | Constraints |
|---|---|---|
| id | BIGSERIAL | PRIMARY KEY |
| username | VARCHAR | NOT NULL, UNIQUE |
| email | VARCHAR | NOT NULL |
| role | VARCHAR | ADMIN / MEMBER |
| created_at | TIMESTAMP | auto-set on insert |

**tasks**

| Column | Type | Constraints |
|---|---|---|
| id | BIGSERIAL | PRIMARY KEY |
| title | VARCHAR | NOT NULL |
| description | VARCHAR | nullable |
| status | VARCHAR | TODO / IN_PROGRESS / DONE |
| due_date | DATE | nullable |
| assigned_to | BIGINT | FK → users.id |
| created_at | TIMESTAMP | auto-set on insert |

---

## Getting Started

### Prerequisites
- Java 21+
- PostgreSQL running locally
- Maven

### 1. Create the database
```sql
CREATE DATABASE taskmanager_db;
```

### 2. Configure `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taskmanager_db
spring.datasource.username=postgres
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### 3. Run
```bash
mvn spring-boot:run
```

App starts on `http://localhost:8080`. Hibernate auto-creates tables on first run.

---

## API Reference

### Users

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/users` | Create a user |
| GET | `/api/users` | List all users |
| GET | `/api/users/{id}` | Get user by ID |
| DELETE | `/api/users/{id}` | Delete a user |

### Tasks

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/tasks` | Create a task |
| GET | `/api/tasks?page=0&size=10` | All tasks, paginated |
| GET | `/api/tasks/{id}` | Get task by ID |
| GET | `/api/tasks/user/{userId}` | Tasks assigned to a user |
| GET | `/api/tasks/status/{status}` | Filter by TODO / IN_PROGRESS / DONE |
| PUT | `/api/tasks/{id}` | Update a task |
| DELETE | `/api/tasks/{id}` | Delete a task |

---

## Example Requests

### Create a user
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"thilina","email":"thilina@test.com","role":"ADMIN"}'
```

### Create a task
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Build the API","description":"Finish all endpoints","status":"TODO","assignedToId":1}'
```

### Get all tasks (paginated)
```bash
curl "http://localhost:8080/api/tasks?page=0&size=10"
```

### Filter by status
```bash
curl "http://localhost:8080/api/tasks/status/TODO?page=0&size=5"
```

### Update a task
```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Build the API","status":"IN_PROGRESS","assignedToId":1}'
```

---

## Key Concepts Practiced

- **Layered architecture** — Controller → Service → Repository separation
- **Spring Data JPA** — query derivation, `Optional<T>`, pagination with `Pageable`
- **Entity relationships** — `@ManyToOne` with lazy fetching
- **DTOs** — separating API contract from DB model using Java `record`
- **Enums** — stored as strings with `@Enumerated(EnumType.STRING)`
- **Lifecycle hooks** — `@PrePersist` for auto-setting `createdAt` and default status
- **Global error handling** — `@RestControllerAdvice`
- **Lombok** — `@Data`, `@Builder`, `@RequiredArgsConstructor`

---

## What's Next

- [ ] Input validation (`@NotBlank`, `@Email`)
- [ ] Custom typed exceptions (`ResourceNotFoundException`)
- [ ] Spring Security + JWT authentication
- [ ] Unit tests with JUnit 5 + Mockito

---

## License

MIT
