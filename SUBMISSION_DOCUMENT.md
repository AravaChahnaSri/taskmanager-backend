# Take-Home Assignment Submission Document
## Role: Java Full Stack Developer Intern
## Project Title: AI-Powered Task Management Portal

---

## 🔗 Project Links

1. **Frontend Deployment (Vercel)**: Live Production Web App
2. **Backend API Deployment (Render)**: `https://taskmanager-backend-49bi.onrender.com`
3. **Frontend GitHub Repository**: `https://github.com/AravaChahnaSri/taskmanager-frontend`
4. **Backend GitHub Repository**: `https://github.com/AravaChahnaSri/taskmanager-backend`

---

## 🏗️ System Architecture & Tech Stack

### Backend (Spring Boot 3.4+ / Java 21)
- **Architecture**: Layered (Controller, Service, Repository, Entity, DTO)
- **Security**: Spring Security + Stateless JWT Token Authentication + BCrypt Password Encoder
- **Database**: Cloud MySQL (Railway) managed via Spring Data JPA / Hibernate ORM
- **Containerization & Hosting**: Multi-stage Docker build hosted on Render

### Frontend (React 19 + Vite 8)
- **UI Framework**: React with Tailwind CSS v4
- **State Management**: React State (`useState`, `useContext`) + LocalStorage JWT token management
- **Routing**: React Router DOM v7 with Vercel SPA rewrite rules (`vercel.json`)
- **Hosting**: Vercel

---

## 📌 1. Assumptions

1. **Stateless Authentication**: Users receive a signed JWT token upon successful authentication. Each subsequent protected REST request includes the token in the `Authorization: Bearer <token>` HTTP header.
2. **Task Ownership Isolation**: Each user can only view, edit, or delete their own tasks based on user ID extracted from JWT token security context.
3. **Database Schema Auto-Generation**: Hibernate `ddl-auto=update` handles creation of tables (`users`, `tasks`, `task_blockchain_ledger`) on cloud database startup.
4. **AI Resilience**: If Google Gemini API is unavailable or rate-limited, the application gracefully falls back to default fallback values without crashing the user flow.

---

## 🤖 2. AI Workflow (Option A — AI Task Description & Metadata Generator)

### Integration:
- Uses **Google Gemini 1.5 Flash API** (`https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent`).
- Authenticates securely via `x-goog-api-key` header supplied from environment configuration (`GEMINI_API_KEY`).

### Execution Flow:
1. User enters a **Task Title** (e.g. *"Prepare client presentation"*).
2. User clicks the **"✨ Auto-Generate with AI"** button on the TaskForm component.
3. The frontend triggers 3 REST calls to `/api/ai/description`, `/api/ai/priority`, and `/api/ai/estimate`.
4. `AiService` sends structured prompt constraints to Gemini API:
   - **Description**: Generates 1-2 concise, actionable sentences.
   - **Priority**: Evaluates complexity and assigns `LOW`, `MEDIUM`, or `HIGH`.
   - **Time Estimate**: Calculates realistic effort (e.g. *"4-6 hours"*).
5. The form fields auto-populate in real time!

---

## ⛓️ 3. Blockchain Implementation (Option A — Immutable Task History)

### Concept & Design:
To fulfill the **Bonus Requirement (+10% Bonus Points)**, we implemented an **Immutable Task Audit Ledger** using SHA-256 cryptographic block hashing.

### How It Works:
1. Every task event (`TASK_CREATED`, `STATUS_UPDATED`, `TASK_DELETED`) triggers `BlockchainService.recordBlock(...)`.
2. Each block stores:
   - `Block ID`: Unique sequential index
   - `Task ID`: Associated task
   - `Action`: Event description (e.g. `STATUS_UPDATED: DONE`)
   - `Timestamp`: Precise timestamp
   - `Previous Hash`: SHA-256 hash of the preceding block (or genesis hash for block #1)
   - `Block Hash`: Cryptographic hash generated from `(taskId + action + timestamp + previousHash)`
3. **Tamper-Evident Verification**: The `/api/blockchain/verify` endpoint dynamically recalculates every block hash from genesis to tip. If any block data is modified, the hash mismatch is instantly flagged!
4. **UI Integration**: Frontend includes a live **"🔗 Blockchain Audit"** modal displaying the block chain and real-time cryptographic integrity check.

---

## 🗄️ 4. Database Schema (ER Diagram Overview)

```
 ┌────────────────────────┐          ┌────────────────────────┐
 │        users           │          │         tasks          │
 ├────────────────────────┤          ├────────────────────────┤
 │ id (PK, Long)          │1        *│ id (PK, Long)          │
 │ name (VARCHAR)         ├─────────<│ user_id (FK, Long)      │
 │ email (VARCHAR, UNIQUE)│          │ title (VARCHAR)        │
 │ password (VARCHAR)     │          │ description (TEXT)     │
 │ created_at (DATETIME)  │          │ status (VARCHAR)       │
 └────────────────────────┘          │ priority (VARCHAR)     │
                                     │ due_date (DATE)        │
                                     │ estimated_time (VARCHAR)│
                                     │ created_at (DATETIME)  │
                                     └────────────────────────┘

 ┌───────────────────────────────────┐
 │       task_blockchain_ledger      │
 ├───────────────────────────────────┤
 │ id (PK, Long)                     │
 │ task_id (Long)                    │
 │ action (VARCHAR)                  │
 │ timestamp (DATETIME)              │
 │ previous_hash (VARCHAR)           │
 │ block_hash (VARCHAR)              │
 └───────────────────────────────────┘
```

---

## 📹 5. Demo Video Script (3–5 Minutes)

### Introduction (0:00 - 0:45)
- *"Hi everyone, my name is Chanu. Today I am presenting my submission for the Java Full Stack Developer Intern Take-Home Assignment: **TaskFlow - AI-Powered Task Management Portal**."*
- *"The tech stack includes **Spring Boot 3** and **Java 21** for the backend, **Cloud MySQL** hosted on Railway, and **React + Vite with Tailwind CSS** deployed on Vercel."*

### Feature Walkthrough (0:45 - 2:15)
- **User Authentication**: Demonstrate User Registration & Login with JWT token storage.
- **Task Management**: Show creating, editing, and filtering tasks across `TODO`, `IN_PROGRESS`, and `DONE` statuses.

### AI Automation Feature (2:15 - 3:30)
- Demonstrate typing a Task Title (e.g. *"Prepare quarterly report"*).
- Click **"✨ Auto-Generate with AI"**.
- Explain how Gemini 1.5 Flash API generates description, priority (`HIGH`), and time estimate (`3-4 hours`).

### Blockchain Audit & Conclusion (3:30 - 4:30)
- Click the **"🔗 Blockchain Audit"** button.
- Explain the SHA-256 cryptographic chain recording all task events and verify tamper-proof status.
- Show live deployment on Render and Vercel.

---

## 🚀 Deployment Summary
- **Frontend**: Vercel (Auto-deploy from `main` branch)
- **Backend**: Render (Docker multi-stage container auto-deploy)
- **Database**: Railway (Cloud MySQL)
