# Spendly - Technical & Architectural Documentation

This document describes the architectural design and technical decisions of the Spendly application, designed to meet the requirements of the **Mobile Computing (CMOV)** course at FEUP.

## 1. Architectural Overview (MVVM + Clean Architecture)
The project strictly adheres to separation of concerns by dividing the application into three distinct layers. This satisfies **Requirement 3.8: Architectural Organization**.

### A. Presentation Layer (UI)
- **Technology:** Jetpack Compose, Material Design 3.
- **State Management:** ViewModels (e.g., `HomeViewModel`) expose a single, immutable `UiState` via `StateFlow`.
- **Navigation:** Managed via a centralized `NavHost` in `SpendlyNavHost.kt`, implementing at least 4 distinct screens (**Requirement 3.2**).

### B. Domain Layer (Business Logic)
- **Technology:** Pure Kotlin, Coroutines.
- **Components:** Contains Use Cases (e.g., `GetExpensesUseCase`, `AddExpenseUseCase`) and Repository Interfaces. This ensures business rules are independent of implementation details.

### C. Data Layer (Persistence & Networking)
- **Local Source (Room):** Acts as the **Single Source of Truth**. The UI observes the local database, ensuring the app is usable 100% offline (**Requirement 3.3 & 3.7**).
- **Remote Source (Retrofit):** Handles REST/HTTP communication with the backend (GET/POST) asynchronously (**Requirement 3.4 & 3.5**).
- **Settings (DataStore):** Persists user preferences (e.g., Currency) on disk (**Requirement 3.3**).

---

## 2. Key Technical Implementations

### ✅ Offline-First Strategy (Requirement 3.7)
When a user adds an expense:
1. It is instantly saved to Room with `isSynced = false`.
2. The UI updates immediately from the local Flow.
3. An immediate network sync is attempted via Retrofit.
4. If the network is unavailable, a **WorkManager** (`SyncWorker`) is scheduled to retry once connectivity returns (**Requirement 3.7**).

### ✅ Meaningful Sensor Integration (Requirement 3.6)
- **Location Sensor:** The app uses the `FusedLocationProviderClient` to fetch coordinates and the `Geocoder` to automatically tag expenses with a city name.
- **Functional Impact:** Reduces manual data entry by providing geographical context to transactions.

### ✅ Bonus: NFC Integration
- **Concept:** The app supports "NFC Quick Log." Scanning a pre-configured tag (e.g., on a coffee machine) automatically logs a transaction without user input.
- **Implementation:** `MainActivity` intercepts NDEF intents and processes them via `ProcessNfcTagUseCase`.

---

## 3. Data Model Description
- **Expense:** Core entity containing `amount`, `category`, `timestamp`, `location`, `notes`, and `isSynced`.
- **Sync Logic:** The `isSynced` boolean in the Room Database tracks which transactions are backed up to the cloud.

## 4. REST/HTTP Integration
- **POST:** Used to back up individual local expenses to the remote server.
- **GET:** Used to synchronize or restore data from the cloud when the app is installed on a new device.
- **Asynchronicity:** All calls use Kotlin Coroutines and are wrapped in `Result` or `Response` objects to prevent blocking the UI thread (**Requirement 3.5**).
