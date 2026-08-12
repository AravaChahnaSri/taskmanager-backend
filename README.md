# AI-Powered Task Management Portal — Backend

A robust, enterprise-ready Spring Boot 3 REST API backend for managing tasks with Google Gemini AI automation, JWT Security, and an Immutable Blockchain Audit Trail.

---

## 🌟 Features & Technical Highlights

- 🔐 **JWT Authentication & Security**: Stateless JWT authentication, BCrypt password hashing, custom JWT filter, and Spring Security authorization.
- 🤖 **Google Gemini AI Integration**: Automated AI task description generation, priority recommendation, and completion time estimation using Google Gemini 1.5 Flash.
- ⛓️ **Cryptographic Blockchain Audit Ledger (Bonus Feature)**: Tamper-evident SHA-256 block chain audit log recording all task creations, status updates, and deletions.
- 🗄️ **Multi-Database Support**: Configured for Cloud MySQL (Railway) with Hibernate JPA auto-schema generation and PostgreSQL compatibility.
- 🐳 **Dockerized & Cloud Deployed**: Multi-stage Docker build deployed on Render.

---

## 🛠️ Tech Stack

- **Java Version**: Java 21
- **Framework**: Spring Boot 3.4+ / 4.1+
- **Security**: Spring Security, JJWT (io.jsonwebtoken 0.12.6), BCrypt
- **Database / ORM**: Spring Data JPA, Hibernate, MySQL Connector, PostgreSQL Driver
- **AI Integration**: Google Gemini API (`gemini-1.5-flash`)
- **Deployment**: Render (Docker Runtime), Railway (Cloud MySQL)

---

## 📐 Architecture Overview

```
      [ Client (React + Vite) ]
                  │
        (HTTP REST + JWT Bearer)
                  │
                  ▼
   ┌─────────────────────────────┐
   │    Spring Security Filter    │
   └──────────────┬──────────────┘
                  │
                  ▼
   ┌─────────────────────────────┐
   │         Controllers         │
   │ Auth | Task | AI | Block    │
   └──────────────┬──────────────┘
                  │
                  ▼
   ┌─────────────────────────────┐
   │        Service Layer        │
   │ TaskService | AiService...  │
   └──────┬───────────────┬──────┘
          │               │
          ▼               ▼
┌─────────────────┐  ┌───────────────────────┐
│ Gemini 1.5 AI   │  │ Spring Data Repos     │
└─────────────────┘  └───────────┬───────────┘
                                 │
                                 ▼
                     ┌───────────────────────┐
                     │  Railway Cloud MySQL  │
                     └───────────────────────┘
```

---

## 🌐 API Endpoints

### 🔐 Authentication (`/api/auth`)
| Method | Endpoint | Description | Public / Protected |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new user | Public |
| `POST` | `/api/auth/login` | Login & generate JWT Token | Public |

### 📝 Task Management (`/api/tasks`)
| Method | Endpoint | Description | Public / Protected |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/tasks` | Get all tasks for logged-in user | Protected (JWT) |
| `GET` | `/api/tasks/{id}` | Get task by ID | Protected (JWT) |
| `POST` | `/api/tasks` | Create a new task | Protected (JWT) |
| `PUT` | `/api/tasks/{id}` | Update existing task | Protected (JWT) |
| `DELETE` | `/api/tasks/{id}` | Delete task | Protected (JWT) |

### 🤖 AI Automation (`/api/ai`)
| Method | Endpoint | Description | Public / Protected |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/ai/description?title={title}` | Generate AI Task Description | Protected (JWT) |
| `POST` | `/api/ai/priority?title={title}&description={desc}` | Suggest AI Task Priority (`LOW`, `MEDIUM`, `HIGH`) | Protected (JWT) |
| `POST` | `/api/ai/estimate?title={title}&description={desc}` | Estimate Completion Effort | Protected (JWT) |

### ⛓️ Blockchain Audit Trail (`/api/blockchain`)
| Method | Endpoint | Description | Public / Protected |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/blockchain/ledger` | View full SHA-256 block chain audit log | Public |
| `GET` | `/api/blockchain/verify` | Verify cryptographic chain integrity | Public |

---

## ⚡ Setup & Local Execution

1. **Clone the repository**:
   ```bash
   git clone https://github.com/AravaChahnaSri/taskmanager-backend.git
   cd taskmanager-backend
   ```

2. **Configure Environment Variables** in `src/main/resources/application.properties` or system:
   ```properties
   SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/taskmanager_db
   SPRING_DATASOURCE_USERNAME=root
   SPRING_DATASOURCE_PASSWORD=yourpassword
   JWT_SECRET=your_secret_key
   GEMINI_API_KEY=your_gemini_api_key
   ```

3. **Build and Run**:
   ```bash
   ./mvnw clean package -DskipTests
   ./mvnw spring-boot:run
   ```

4. **Docker Execution**:
   ```bash
   docker build -t taskmanager-backend .
   docker run -p 8080:8080 taskmanager-backend
   ```
