********************************** Task Manager API **********************************
A RESTful Task Management API built with Java, Spring Boot, and PostgreSQL. 
Supports user management, task creation, assignment, status tracking, and paginated queries.

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
    └── GlobalExceptionHandler.java  # Centralised error handling

********************************** Architecture **********************************

The project follows a strict 3-layer architecture — each layer has one responsibility and only communicates with the layer directly next to it.

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
│  Business logic, validation, orchestration             │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────┐
│  Repository  (@Repository)                              │
│  Database queries via Spring Data JPA                  │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
               [ PostgreSQL DB ]

********************************** Request Flow **********************************

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


********************************** Key Concepts Practiced **********************************
Layered architecture — Controller → Service → Repository separation
Spring Data JPA — query derivation, Optional<T>, pagination with Pageable
Entity relationships — @ManyToOne / @OneToMany with lazy fetching
DTOs — separating API contract from database model using Java record
Enums — stored as strings with @Enumerated(EnumType.STRING)
Lifecycle hooks — @PrePersist for auto-setting createdAt and default status
Global error handling — @RestControllerAdvice for centralised exception management
Lombok — @Data, @Builder, @RequiredArgsConstructor to eliminate boilerplate
