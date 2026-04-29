# 🧳 JobPortal API

A production-ready RESTful Job Search API built with **Spring Boot 3**, **PostgreSQL**, **Redis**, and **Docker**. Features JWT authentication, role-based access control, rate limiting, token blacklisting, and full Swagger documentation.

## ✨ Features

- ✅ JWT-based stateless authentication
- ✅ Role-based access control (`ROLE_USER`, `ROLE_ADMIN`)
- ✅ Job search with multi-filter support (keyword, location, job type)
- ✅ JPA Specifications for dynamic query building
- ✅ Pagination & sorting with validated bounds
- ✅ Rate limiting per IP (5 requests/minute)
- ✅ Redis-backed token blacklist with logout support
- ✅ Structured error responses on all endpoints
- ✅ Swagger / OpenAPI 3 documentation with Bearer auth
- ✅ Multi-stage Dockerfile for optimized image size
- ✅ All secrets managed via environment variables

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.3.4 |
| Security | Spring Security 6 + JWT (jjwt 0.11.5) |
| Database | PostgreSQL 15 |
| Cache / Session | Redis (Lettuce) |
| ORM | Hibernate / Spring Data JPA |
| Mapping | MapStruct |
| Validation | Jakarta Bean Validation |
| Rate Limiting | Bucket4j |
| Documentation | SpringDoc OpenAPI (Swagger UI) |
| Containerization | Docker + Docker Compose |
| Build Tool | Maven |

---

## 🏗 Architecture

```
┌─────────────────────────────────────────────────┐
│                   Client / Swagger UI            │
└───────────────────────┬─────────────────────────┘
                        │
              ┌─────────▼──────────┐
              │   RateLimitFilter  │  (Bucket4j, per IP)
              └─────────┬──────────┘
                        │
              ┌─────────▼──────────┐
              │ JwtAuthentication  │  (Token validation + Blacklist check)
              │      Filter        │
              └─────────┬──────────┘
                        │
          ┌─────────────▼─────────────┐
          │        Controllers         │
          │  AuthController            │
          │  JobController             │
          └─────────────┬─────────────┘
                        │
          ┌─────────────▼─────────────┐
          │         Services           │
          │  AuthService               │
          │  JobServiceImpl            │
          │  TokenBlacklistService     │
          └──────┬──────────┬─────────┘
                 │          │
        ┌────────▼───┐  ┌───▼────────┐
        │ PostgreSQL  │  │   Redis    │
        │  (JPA)      │  │ (Blacklist)│
        └─────────────┘  └────────────┘
```

---

## 🚀 Getting Started

### Prerequisites

- [Docker](https://www.docker.com/) & Docker Compose
- Java 17+ (only if running locally without Docker)

### Run with Docker (Recommended)

**1. Clone the repository:**
```bash
git clone https://github.com/your-username/jobportal-api.git
cd jobportal-api
```

**2. Create a `.env` file in the project root:**
```env
DB_USERNAME=postgres
DB_PASSWORD=yourpassword
JWT_SECRET_KEY=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
JWT_EXPIRATION=86400000
REDIS_HOST=redis
REDIS_PORT=6379
```

**3. Build and start all services:**
```bash
docker compose up --build
```

**4. Open Swagger UI:**
```
http://localhost:8080/swagger-ui/index.html
```

### Run Locally (Without Docker)

**1. Start PostgreSQL and Redis locally, then set environment variables:**
```bash
export DB_USERNAME=postgres
export DB_PASSWORD=yourpassword
export JWT_SECRET_KEY=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
export JWT_EXPIRATION=86400000
export REDIS_HOST=localhost
export REDIS_PORT=6379
```

**2. Run the application:**
```bash
./mvnw spring-boot:run
```

---

## 🔐 Environment Variables

| Variable | Description | Example |
|---|---|---|
| `DB_USERNAME` | PostgreSQL username | `postgres` |
| `DB_PASSWORD` | PostgreSQL password | `secret` |
| `JWT_SECRET_KEY` | Base64-encoded HMAC secret (min 256-bit) | `404E63...` |
| `JWT_EXPIRATION` | Token expiry in milliseconds | `86400000` (24h) |
| `REDIS_HOST` | Redis hostname | `redis` (Docker) / `localhost` |
| `REDIS_PORT` | Redis port | `6379` |

> ⚠️ Never commit the `.env` file to version control. It is listed in `.gitignore`.

---

## 📡 API Endpoints

### Auth

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/v1/auth/register` | Public | Register a new user |
| `POST` | `/api/v1/auth/login` | Public | Login and receive JWT token |
| `POST` | `/api/v1/auth/logout` | Authenticated | Invalidate current token |

### Jobs

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/v1/jobs/search` | Public | Search jobs with filters |
| `POST` | `/api/v1/jobs/create` | `ROLE_ADMIN` | Create a new job listing |

### Search Query Parameters

| Parameter | Type | Default | Description |
|---|---|---|---|
| `keyword` | String | — | Search in title and description |
| `location` | String | — | Filter by location |
| `jobType` | Enum | — | `FULL_TIME`, `PART_TIME`, `REMOTE`, `CONTRACT` |
| `page` | Integer | `0` | Page number (min: 0) |
| `size` | Integer | `10` | Page size (min: 1, max: 100) |
| `sortBy` | String | `createdAt` | `createdAt`, `salary`, `title`, `companyName` |

---

## 🔑 Authentication Flow

```
1. POST /api/v1/auth/register  →  201 Created  +  JWT token
2. POST /api/v1/auth/login     →  200 OK        +  JWT token
3. Use token in header:  Authorization: Bearer <token>
4. POST /api/v1/auth/logout    →  204 No Content (token blacklisted in Redis)
```

**Register request body:**
```json
{
  "firstname": "John",
  "lastname": "Doe",
  "email": "john@example.com",
  "password": "secret123"
}
```

**Login request body:**
```json
{
  "email": "john@example.com",
  "password": "secret123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGci...",
  "tokenType": "Bearer"
}
```

---

## ⚡ Rate Limiting

Requests are rate-limited **per IP address** using Bucket4j:

- **Limit:** 5 requests per minute
- **Scope:** All endpoints except `/api/v1/auth/**` and Swagger UI
- **Exceeded response:** `429 Too Many Requests`

---

## 🛡 Security

| Feature | Implementation |
|---|---|
| Password hashing | BCrypt |
| Token format | JWT (HS256) |
| Session policy | Stateless |
| Token revocation | Redis blacklist (TTL = token expiry) |
| Role enforcement | `@PreAuthorize` + `@EnableMethodSecurity` |
| CORS | Configurable origin allowlist |
| Secret management | Environment variables only — no hardcoded credentials |

---

## 📁 Project Structure

```
src/main/java/com/example/
├── config/
│   ├── AppConfig.java              # AuthProvider, PasswordEncoder, Swagger config
│   └── DataInitializer.java        # Seeds ROLE_USER and ROLE_ADMIN on startup
├── controller/
│   ├── AuthController.java         # Register, Login, Logout
│   └── JobController.java          # Search, Create
├── dto/
│   ├── AuthResponse.java
│   ├── JobCreateDto.java
│   ├── JobResponseDto.java
│   ├── JobSearchRequestDto.java
│   ├── LoginRequest.java
│   └── RegisterRequest.java
├── entity/
│   ├── Job.java
│   ├── Role.java
│   └── User.java
├── enums/
│   ├── JobType.java
│   └── RoleType.java
├── exception/
│   ├── ErrorDetails.java
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── UserNotFoundException.java
├── filter/
│   └── RateLimitFilter.java        # Bucket4j IP-based rate limiting
├── mapper/
│   └── JobMapper.java              # MapStruct mapper
├── repository/
│   ├── JobRepository.java
│   ├── RoleRepository.java
│   ├── UserRepository.java
│   └── specification/
│       └── JobSpecification.java   # Dynamic JPA Specifications
├── security/
│   ├── CustomUserDetailsService.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtService.java
│   └── SecurityConfig.java
└── service/
    ├── AuthService.java
    ├── JobService.java             # Interface
    ├── TokenBlacklistService.java  # Redis-backed token blacklist
    └── impls/
        └── JobServiceImpl.java
```

---

## 📄 License

This project is for educational purposes.
