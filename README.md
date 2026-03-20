# Secure Library Management System
 
**Secure Software Development — Group 01**
 
A secure web application for library management built with Java Spring Boot, demonstrating security-first design across the full software development lifecycle.
 
---
 
## Table of Contents
 
- [Project Overview](#project-overview)
- [Technologies Used](#technologies-used)
- [How to Run](#how-to-run)
- [Default Test Accounts](#default-test-accounts)
- [API Endpoints](#api-endpoints)
- [Security Features](#security-features)
- [Project Structure](#project-structure)
 
---
 
## Project Overview
 
The application supports two roles:
 
- **USER** — register, log in, browse and search books, submit borrow requests, track request status
- **LIBRARIAN** — add/edit/delete books, approve or reject borrow requests, view all users
 
Security is enforced at every layer — input validation, parameterised queries, role-based access control, JWT authentication, error masking, and audit logging.
 
---
 
## Technologies Used
 
| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2 |
| Security | Spring Security 6, JWT (jjwt 0.12.5, HMAC-SHA256) |
| Persistence | Spring Data JPA, H2 in-memory database |
| Validation | Jakarta Bean Validation |
| Frontend | HTML5, Vanilla JavaScript, CSS3 |
| Build | Maven 3.x |
| SAST | SonarQube for IDE (IntelliJ plugin) |
| DAST | OWASP ZAP |
| CI/CD | GitHub Actions (`.github/workflows/security-pipeline.yml`) |
 
---
 
## How to Run
 
### Prerequisites
 
- Java 17 or higher
- Maven 3.8 or higher
 
Check your versions:
```bash
java -version
mvn -version
```
 
### Steps
 
**1. Clone or extract the project**
```bash
cd library-app
```
 
**2. Run the application**
```bash
mvn spring-boot:run
```
 
On Windows PowerShell (if Maven is not installed globally):
```powershell
.\mvnw.cmd spring-boot:run
```
 
**3. Open in browser**
```
http://localhost:8080
```
 
**4. Stop the application**
 
Press `Ctrl + C` in the terminal.
 
**5. Run tests**
```bash
mvn test
```
 
### H2 Console (development only)
 
While the app is running, the database console is available at:
```
http://localhost:8080/h2-console
```
 
| Field | Value |
|---|---|
| JDBC URL | `jdbc:h2:mem:librarydb` |
| Username | `sa` |
| Password | *(leave blank)* |
 
> Use this to verify that passwords are stored as BCrypt hashes (starting with `$2a$10$`), never as plain text.
 
---
 
## Default Test Accounts
 
These accounts are created automatically when the application starts via `DataInitializer.java`.
 
| Username | Password | Role | Redirects to |
|---|---|---|---|
| `admin` | `Admin1234!` | LIBRARIAN | Admin Panel |
| `user1` | `User1234!` | USER | Book Catalog |
 
> Passwords are hashed using BCrypt before storage — plain text passwords are never persisted.
 
---
 
## API Endpoints
 
### Authentication (public)
 
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user account |
| POST | `/api/auth/login` | Log in and receive a JWT token |
 
### Books (GET is public, write requires LIBRARIAN)
 
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/books` | Public | List all books |
| GET | `/api/books/{id}` | Public | Get book by ID |
| GET | `/api/books/search?keyword=` | Public | Search by title, author or category |
| POST | `/api/books` | LIBRARIAN | Add a new book |
| PUT | `/api/books/{id}` | LIBRARIAN | Update a book |
| DELETE | `/api/books/{id}` | LIBRARIAN | Delete a book |
 
### Borrow Requests
 
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/borrow/request/{bookId}` | Authenticated | Submit a borrow request |
| GET | `/api/borrow/my-requests` | Authenticated | View own borrow requests |
| GET | `/api/borrow/all` | LIBRARIAN | View all borrow requests |
| PUT | `/api/borrow/requests/{id}/approve` | LIBRARIAN | Approve a request |
| PUT | `/api/borrow/requests/{id}/reject` | LIBRARIAN | Reject a request |
 
### Users
 
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/users` | LIBRARIAN | List all registered users |
 
---
 
## Security Features
 
### 1. Password hashing
Passwords are hashed using BCrypt with cost factor 10 before storage. Plain text passwords are never persisted or logged.
 
### 2. JWT authentication
Stateless authentication using HMAC-SHA256 signed tokens. The signing secret is loaded from the `JWT_SECRET` environment variable — never hardcoded. Tokens expire after 24 hours.
 
To set a custom JWT secret before running:
```bash
# Linux / macOS
export JWT_SECRET="your-secret-key-at-least-32-characters-long"
 
# Windows PowerShell
$env:JWT_SECRET="your-secret-key-at-least-32-characters-long"
```
 
A development fallback is provided in `application.properties` for local use only.
 
### 3. Role-based access control (RBAC)
Enforced at two independent layers:
- Spring Security filter chain (`SecurityConfig.java`) — route-level restrictions
- `@PreAuthorize("hasRole('LIBRARIAN')")` — method-level restrictions on every admin endpoint
 
### 4. Input validation
All request DTOs use Jakarta Bean Validation annotations (`@NotBlank`, `@Size`, `@Email`, `@Pattern`). Validation is enforced server-side regardless of client-side checks.
 
### 5. SQL injection prevention
All database queries use JPQL named parameters via Spring Data JPA. Raw SQL string concatenation is structurally impossible.
 
### 6. Error masking
`GlobalExceptionHandler` catches all exceptions and returns generic messages to the client. Stack traces, class names, and SQL errors never appear in HTTP responses. Full details are logged server-side only.
 
### 7. Security headers
Configured in `SecurityConfig.java`:
- `X-Frame-Options: SAMEORIGIN` — clickjacking protection
- `X-Content-Type-Options: nosniff` — MIME sniffing prevention
- `Content-Security-Policy: default-src 'self'` — XSS mitigation
- `Strict-Transport-Security: max-age=31536000; includeSubDomains` — HTTPS enforcement
 
### 8. Audit logging
Every security-relevant action is logged with SLF4J:
- Successful and failed login attempts (username only, never password)
- User registration
- Book add, update, delete (with librarian identity)
- Borrow request approval and rejection
 
### 9. XSS prevention
All user-supplied data is passed through `escapeHtml()` before DOM insertion in every HTML page.
 
### 10. No hardcoded secrets
JWT secret, database credentials, and all sensitive values are loaded from environment variables or application properties. No secrets appear in source code.
 
---
 
## Project Structure
 
```
src/
├── main/
│   ├── java/com/library/
│   │   ├── config/
│   │   │   ├── SecurityConfig.java        # Spring Security, RBAC, JWT filter, CORS, headers
│   │   │   └── DataInitializer.java       # Seeds default users and books on startup
│   │   ├── controller/
│   │   │   ├── AuthController.java        # POST /api/auth/register and /login
│   │   │   ├── BookController.java        # GET/POST/PUT/DELETE /api/books
│   │   │   ├── BorrowController.java      # Borrow request endpoints
│   │   │   └── UserController.java        # GET /api/users (librarian only)
│   │   ├── dto/
│   │   │   ├── AuthDtos.java              # RegisterRequest, LoginRequest, JwtResponse
│   │   │   ├── BookDto.java               # Book create/update request
│   │   │   └── BorrowRequestDto.java      # Borrow request response projection
│   │   ├── exception/
│   │   │   ├── GlobalExceptionHandler.java # Catches all exceptions, masks details
│   │   │   └── ResourceNotFoundException.java
│   │   ├── model/
│   │   │   ├── User.java                  # User entity
│   │   │   ├── Book.java                  # Book entity
│   │   │   ├── BorrowRequest.java         # Borrow request entity
│   │   │   └── Role.java                  # USER / LIBRARIAN enum
│   │   ├── repository/
│   │   │   ├── UserRepository.java        # JPA with parameterised JPQL
│   │   │   ├── BookRepository.java        # Search by keyword (JPQL)
│   │   │   └── BorrowRequestRepository.java
│   │   ├── security/
│   │   │   ├── JwtUtils.java              # Token generation and validation
│   │   │   ├── JwtAuthFilter.java         # Validates JWT on every request
│   │   │   └── CustomUserDetailsService.java # Loads users from DB for Spring Security
│   │   └── service/
│   │       ├── UserService.java           # Registration, user lookup
│   │       ├── BookService.java           # Book CRUD and search
│   │       └── BorrowService.java         # Borrow request workflow
│   └── resources/
│       ├── application.properties         # H2, JWT config, logging, error masking
│       └── static/
│           ├── index.html                 # Book catalog
│           ├── login.html                 # Login page
│           ├── register.html              # Registration page
│           ├── admin.html                 # Librarian admin panel
│           ├── my-requests.html           # User borrow request history
│           ├── css/style.css              # Self-hosted styles, no CDN
│           └── js/
│               ├── auth.js                # Token management, escapeHtml, route guards
│               └── api.js                 # Centralised fetch wrapper with JWT header
├── test/
│   └── java/com/library/
│       ├── AuthControllerTest.java        # Unit tests: login, registration validation
│       └── SecurityIntegrationTest.java   # Integration: 401/403 enforcement, auth flow
└── .github/
    └── workflows/
        └── security-pipeline.yml          # CI/CD: SAST + dependency scan + DAST + gate
```
 
---
 
## CI/CD Security Pipeline
 
The pipeline is defined in `.github/workflows/security-pipeline.yml` and runs on every push or pull request to `main`.
 
| Stage | Tool | Gate |
|---|---|---|
| 1. Code push / PR | GitHub trigger | — |
| 2. Build & tests | `mvn verify` + JUnit | Fails if tests break |
| 3. SAST scan | Semgrep `p/java` + `p/owasp-top-ten` | Blocks on HIGH severity |
| 4. Dependency scan | OWASP Dependency-Check | Blocks on CVSS >= 7 |
| 5. Deploy to staging | `java -jar` + health check | — |
| 6. DAST scan | OWASP ZAP baseline + active | Reports runtime vulnerabilities |
| 7. Security gate | Result evaluation | Blocks merge if SAST or DAST failed |
 
`JWT_SECRET` is stored in GitHub Actions Secrets and injected at runtime — never written in YAML.
 
---
 
*Simple, clear, and secure is better than complex and unfinished.*
 
