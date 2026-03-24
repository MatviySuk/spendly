# Spendly - Project Status & Task List

This document tracks the progress of the Spendly Android application against the requirements of the **Mobile Computing (CMOV)** lab assignment.

---

## ✅ Completed Work (Infrastructure & Scaffolding)
*The following items have been fully implemented and verified to compile.*

### 1. Architectural Foundation (Req 3.8)
- [x] **Clean Architecture:** Defined layers for Data, Domain, and Presentation.
- [x] **MVVM Pattern:** ViewModels implemented with `StateFlow` and `UiState` objects.
- [x] **Dependency Injection:** Hilt (Dagger) fully configured across the app.

### 2. Navigation & UI Shell (Req 3.2)
- [x] **Compose Navigation:** `NavHost` implemented with 4 distinct routes.
- [x] **Bottom Navigation:** `MainActivity` features a functional navigation bar.
- [x] **Material 3 Theme:** Full theme support including Dark Mode and Dynamic Color.

### 3. Local Data Management (Req 3.3)
- [x] **Room Database:** Database, Entities, and DAOs configured.
- [x] **Sync Tracking:** `isSynced` flag implemented in the data model for offline-first support.

### 4. Remote & Async (Req 3.4 & 3.5)
- [x] **Retrofit Setup:** API interface and OkHttpClient (with logging) ready.
- [x] **Asynchronicity:** All data operations use Coroutines and Flows.
- [x] **Serialization:** `kotlinx.serialization` integrated.

### 5. Offline & Background (Req 3.7)
- [x] **Connectivity Observation:** `ConnectivityObserver` implemented to detect network changes.
- [x] **Background Sync:** `SyncWorker` (WorkManager) scaffolded for reliable data backup and scheduled in `SpendlyApp`.

---

## 🛠 Remaining Work (Feature Implementation)
*These tasks are assigned to the implementation team (Teammates).*

### 📍 Task 1: Meaningful Sensor Integration (Req 3.6)
- **Goal:** Use the Location sensor to automatically tag expenses.
- [x] Implement `fetchCurrentLocation()` in `AddExpenseViewModel.kt`.
- [x] Use `FusedLocationProviderClient` to get coordinates.
- [x] Use `Geocoder` to convert coordinates into a city name (e.g., "Porto").
- [ ] **Bonus:** Implement "Shake to Clear" using the Accelerometer in the Add Expense screen.

### 📝 Task 2: Expense Entry Forms (Req 3.2 & 3.3)
- **Goal:** Build a functional and user-friendly data entry UI.
- [x] Complete `AddExpenseScreen.kt` UI (Amount input, Category dropdown, Notes field).
- [x] Add input validation (ensure amount is a valid number).
- [x] Implement the "Save" button to trigger the `AddExpenseUseCase`.

### 📊 Task 3: Spending Analysis & Charts (Req 3.2)
- **Goal:** Visualize data as required by the assignment.
- [x] Implement a Category Pie Chart or Bar Chart in `AnalysisScreen.kt`.
- [x] Use the **Vico** library (already in dependencies).
- [x] Logic: Filter and group expenses by category to provide monthly totals.

### ⚙️ Task 4: Persistent User Settings (Req 3.3)
- **Goal:** Save user preferences on disk.
- [x] Implement Budget selection in `SettingsScreen.kt`.
- [x] Save/Load the selection using `UserPreferencesRepository.kt` (Jetpack DataStore).
- [x] **Bonus:** Implement a Manual Sync button that triggers the `SyncWorker`.

### 📱 Task 5: NFC Quick Log (Bonus Req 3.6)
- **Goal:** Extra points for NFC hardware integration.
- [x] Implement NDEF message parsing in `ProcessNfcTagUseCase.kt`.
- [x] Create a "Quick Log" flow where tapping a tag automatically saves a predefined expense.

---

## 🚀 Final Delivery Checklist
- [x] Update Firebase URL in `local.properties` (Done, `BuildConfig.FIREBASE_DB_URL`).
- [ ] Conduct final testing on a physical Android device.
- [ ] Generate the final APK for submission.
- [ ] Record a 2-minute demo video.
