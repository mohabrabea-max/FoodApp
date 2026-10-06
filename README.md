# 🍔 Food Delivery App — Native Android (Production-Ready)

[![Kotlin](https://img.shields.io/badge/Kotlin-2.x-7F52FF.svg?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4.svg?style=flat-square&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-Feature--First%20Clean%20%2B%20MVI/MVVM-00C853.svg?style=flat-square)](#-architecture--design-patterns)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-24-orange.svg?style=flat-square)](#)

A fully-featured, production-ready Android application for food delivery, built with **Kotlin** and **Jetpack Compose**. Engineered following **Feature-First Clean Architecture**, **Unidirectional Data Flow (UDF)**, and an **Offline-First Synchronization Engine**.

---

## 📸 App Screenshots

<img width="1672" height="941" alt="file_00000000bf5081f4b44ac069dd0c89f5" src="https://github.com/user-attachments/assets/b0d51a30-d69b-4698-8633-07fb783c9b16" />

<img width="1672" height="941" alt="file_000000001fe88210b35af2722d7b5e83" src="https://github.com/user-attachments/assets/196011bd-e4bc-4993-8566-1ffc5cf1733f" />

---

## 🏛️ Architecture & Design Patterns

The codebase is structured around **Feature-First Modular Architecture** combined with **Clean Architecture** layers inside core and individual features.

```text
                  ┌─────────────────────────────────────────┐
                  │              UI / Compose               │
                  └──────────────────┬──────────────────────┘
                                     │ User Intents / Actions
                                     ▼
                  ┌─────────────────────────────────────────┐
                  │            ViewModel / MVI              │
                  │   StateFlow / SharedFlow (UI State)     │
                  └──────────────────┬──────────────────────┘
                                     │ Domain Models / Use Cases
                                     ▼
                  ┌─────────────────────────────────────────┐
                  │             Domain Layer                │
                  └──────────────────┬──────────────────────┘
                                     │ Repositories Interface
                                     ▼
                  ┌─────────────────────────────────────────┐
                  │               Data Layer                │
                  │  (Local Room DB ◄─► Remote Sync Engine) │
                  └──────────────────┬──────────────────────┘

```

* **Feature-First Package Structure:** High cohesion and loose coupling between app flows.
* **MVI / MVVM Pattern:** Reactive state management using `StateFlow` and `SharedFlow` with single-source-of-truth (SSOT) principles.
* **Offline-First Sync Engine:** Bi-directional real-time sync mechanism between **Room DB** (Local Source of Truth) and **Firebase Realtime Database**, preventing duplicate mutations and ensuring consistency under low/no connectivity.
* **Concurrency:** Structured Concurrency using **Kotlin Coroutines** and scoped Dispatchers (`IO`, `Default`, `Main`).

---

## 📂 Project Structure

```text
com.example.applicationhome/
├── core/                         # Shared Infrastructure & Core Logic
│   ├── data/                     # Core Data Layer Implementation
│   │   ├── datastore/            # Preferences & Key-Value Storage (DataStore)
│   │   ├── local/                # Room DB Entities, DAOs & Database Instance
│   │   ├── mapper/               # Data Mappers (Entity <-> Domain)
│   │   ├── remote/               # Retrofit APIs, Supabase & Firebase Clients
│   │   └── repository/           # Repository Implementations & Offline Sync Engine
│   ├── domain/                   # Business Logic Layer & Core Models
│   │   ├── exception/            # Custom Domain Failures & Exception Handling
│   │   ├── model/                # Core Domain Entities
│   │   ├── module/               # Dependency Injection Modules / Core Scopes
│   │   ├── repository/           # Core Repository Interfaces
│   │   └── usecase/              # Shared Application Use Cases
│   └── ui/                       # Shared Design System & Base UI Components
│       ├── components/           # Reusable Jetpack Compose Components
│       ├── mapper/               # Domain-to-UI Mappers
│       ├── model/                # UI States & Screen Models
│       └── theme/                # Material Design Theme, Colors & Typography
└── features/                     # Feature-First Architectural Modules
    └── feature/                  # Feature UI, ViewModels & State Management
        └── ui/

```

---

## 🛠️ Tech Stack & Key Libraries

* **Language:** Kotlin (2.2.10)
* **UI Framework:** Jetpack Compose (Declarative UI) + Material 3 Design
* **Networking & APIs:** Retrofit 2 + OkHttp3 + Gson
* **State & Concurrency:** Kotlin Coroutines, Kotlin Flow (`StateFlow`, `SharedFlow`)
* **Local Persistence:** Room Database (SQLite abstraction) & DataStore
* **Background Tasks:** AndroidX WorkManager (Reliable offline-to-online data sync)
* **Remote Data & Auth:**
* **Firebase Realtime Database:** Menu items, real-time order state, restaurant metadata.
* **Supabase:** Authentication & Phone OTP validation.


* **Location & Maps:** Google Maps SDK + Fused Location Provider API
* **DI & Architecture Helpers:** ViewModel, Lifecycle KTX, Navigation Compose

---

## ⚙️️ Environment & Configuration Setup

This project requires external service credentials. Ensure you configure local environment variables before compiling.

### 1. External Keys Configuration

Add the following properties to your local `local.properties` file:

```properties
# Supabase Auth Setup
SUPABASE_URL="https://YOUR_SUPABASE_INSTANCE.supabase.co"
SUPABASE_ANON_KEY="YOUR_SUPABASE_ANON_KEY"

# Google Maps API
MAPS_API_KEY="YOUR_GOOGLE_MAPS_API_KEY"

# Base REST Endpoints
BASE_URL="https://api.yourdomain.com/v1/"

```

### 2. Firebase Integration

* Place your generated `google-services.json` inside the `app/` directory.

---

## 🧪 Technical Highlights & Engineering Decisions

1. **Conflict-Free Sync Engine:** Built custom synchronization handlers using Room transactional queries and Firebase snapshot listeners to resolve data races during offline-to-online transitions.
2. **Reliable Offline Sync via WorkManager:** Enqueued background workers with `NetworkType.CONNECTED` constraints to ensure favorite items toggled offline are safely synced with remote servers once the device re-establishes internet connectivity.
3. **Localization & RTL Support:** Implemented dynamic `Locale` switching at runtime without activity recreation issues, providing full LTR/RTL layout support for Arabic and English.
4. **Memory & Performance Optimization:** Zero memory leaks on Compose screen navigation, optimized recompositions using `Key` and `remember` primitives, and zero main-thread blocking operations.

---

## 📄 License

Distributed under the **MIT License**. See `LICENSE` for details.

```

```
