# Learn Backend

Spring Boot backend for the **Learn** mobile learning app. Handles user accounts, deck/question storage,
solo quiz attempts, and battle session records.

Slide parsing and AI question generation are handled by a separate Python microservice. This backend does
not reimplement that logic — it only exposes an endpoint (`POST /api/decks/{deckId}/questions/bulk`) to
receive and persist the Python service's generated questions.

> **Stage 1 scope:** core CRUD backbone only. Authentication/JWT, real-time battle logic, and analytics
> endpoints are not implemented yet.

## Tech stack

- Java 17
- Spring Boot 3.3 (Web, Data JPA, Validation)
- PostgreSQL
- Maven
- Lombok

## Environment variables

The application reads its database connection entirely from environment variables — no credentials are
hardcoded anywhere in the repo.

| Variable      | Description                              | Example                                                    |
|---------------|------------------------------------------|------------------------------------------------------------|
| `DB_URL`      | JDBC URL for the PostgreSQL database     | `jdbc:postgresql://your-neon-host/your-db?sslmode=require` |
| `DB_USERNAME` | Database username                        | `learn_user`                                               |
| `DB_PASSWORD` | Database password                        | `changeme`                                                 |
| `PORT`        | (Optional) HTTP port, defaults to `8080` | `8080`                                                     |

### Option A: `.env` file (recommended)

The project uses [`spring-dotenv`](https://github.com/paulschwarz/spring-dotenv) to automatically load a
`.env` file at the project root into the Spring environment on startup, so you don't have to export
variables manually every session.

1. Copy the example file:

   ```bash
   cp .env.example .env
   ```

2. Edit `.env` and fill in your real Neon (or other PostgreSQL) credentials:

   ```dotenv
   DB_URL=jdbc:postgresql://your-neon-host/your-db?sslmode=require
   DB_USERNAME=your_username
   DB_PASSWORD=your_password
   ```

`.env` is already listed in `.gitignore` and will never be committed — only `.env.example` (with
placeholder values) is tracked in the repo.

### Option B: shell environment variables

If you'd rather not use a `.env` file, you can export the variables directly, e.g. on PowerShell:

```powershell
$env:DB_URL = "jdbc:postgresql://your-neon-host/your-db?sslmode=require"
$env:DB_USERNAME = "your_username"
$env:DB_PASSWORD = "your_password"
```

or on bash/macOS/Linux:

```bash
export DB_URL=jdbc:postgresql://your-neon-host/your-db?sslmode=require
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
```

## Running locally

1. Make sure the PostgreSQL database referenced by `DB_URL` exists and is reachable (e.g. a Neon project).
2. Set up your credentials via `.env` (Option A above) or exported shell variables (Option B).
3. Run:

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`. Tables are created/updated automatically on startup
against the target database, since `spring.jpa.hibernate.ddl-auto` is set to `update` — no manual schema
setup is needed the first time you point this at a fresh database.

## API overview

| Method | Path                                 | Description                                      |
|--------|--------------------------------------|--------------------------------------------------|
| POST   | `/api/users`                         | Register a new user                              |
| POST   | `/api/users/login`                   | Log in with email + password                     |
| GET    | `/api/users/{id}`                    | Fetch a user by id                               |
| POST   | `/api/decks`                         | Create a new deck                                |
| GET    | `/api/decks/{id}`                    | Fetch a deck                                     |
| GET    | `/api/decks/user/{userId}`           | List all decks owned by a user                   |
| POST   | `/api/decks/{deckId}/questions/bulk` | Save a batch of AI-generated questions to a deck |
| GET    | `/api/decks/{deckId}/questions`      | List all questions for a deck                    |
| POST   | `/api/quiz-attempts`                 | Submit a completed solo quiz attempt             |
| GET    | `/api/quiz-attempts/user/{userId}`   | List a user's quiz attempt history               |

## Example requests

### Register a user

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "student@example.com",
    "username": "studious_stu",
    "password": "hunter22"
  }'
```

### Log in

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "student@example.com",
    "password": "hunter22"
  }'
```

Returns `200 OK` with the same `UserResponse` shape as registration on success, or `401 Unauthorized` with
`{"error": "Invalid credentials"}` if the email doesn't exist or the password is wrong — the response is
identical in both cases so a client can't use it to enumerate registered emails. This endpoint is not JWT-based
yet; it's a plain credential check for the upcoming React Native app, with token-based auth to follow in a
later stage.

> No schema change was needed for this endpoint — it only reads the existing `users` table — so a plain
> restart/redeploy is enough; no migration step required.

### Create a deck

```bash
curl -X POST http://localhost:8080/api/decks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Intro to Cell Biology",
    "subject": "Biology",
    "level": "SECONDARY",
    "sourceFileRef": "slides/cell-biology-101.pdf",
    "ownerId": "00000000-0000-0000-0000-000000000000"
  }'
```

### Bulk-save AI-generated questions for a deck

```bash
curl -X POST http://localhost:8080/api/decks/11111111-1111-1111-1111-111111111111/questions/bulk \
  -H "Content-Type: application/json" \
  -d '{
    "questions": [
      {
        "questionText": "What is the powerhouse of the cell?",
        "options": ["Nucleus", "Mitochondria", "Ribosome", "Golgi apparatus"],
        "correctAnswer": "Mitochondria",
        "difficulty": "EASY",
        "topicTag": "cell-organelles"
      }
    ]
  }'
```

### Submit a solo quiz attempt

```bash
curl -X POST http://localhost:8080/api/quiz-attempts \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "00000000-0000-0000-0000-000000000000",
    "deckId": "11111111-1111-1111-1111-111111111111",
    "score": 8,
    "answersJson": "{\"q1\":\"Mitochondria\"}"
  }'
```

## Project structure

```text
src/main/java/com/learn/backend
├── config      # cross-cutting config (JPA converters, etc.)
├── controller  # REST controllers
├── dto         # request/response DTOs
├── entity      # JPA entities
├── enums       # shared enums (Level, Difficulty, BattleStatus)
├── exception   # custom exceptions + @RestControllerAdvice handler
├── repository  # Spring Data JPA repositories
└── service     # business logic
```
