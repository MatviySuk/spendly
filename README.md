# Spendly - Android Spending Tracker

Spendly is a native Android application developed for the **Mobile Computing (CMOV)** course at FEUP. It is a high-performance, architecturally mature solution for tracking daily expenses, analyzing spending habits, and synchronizing data with a remote cloud backend.

---

## 🚀 Project Overview
Spendly is designed with an **Offline-First** philosophy, ensuring users can log transactions regardless of connectivity. Data is synchronized seamlessly in the background once a connection is established.

### Key Features
*   **Persistent Logging:** Local storage using Room Database.
*   **Remote Sync:** REST/HTTP integration via Retrofit.
*   **Contextual Data:** Automatic location tagging for expenses.
*   **Advanced Analysis:** Visual spending trends and category breakdowns.
*   **NFC Quick Log:** Bonus support for tapping tags to log frequent expenses.

---

## 🛠 Technical Stack
*   **Language:** Kotlin
*   **UI:** Jetpack Compose (Material 3)
*   **Architecture:** Clean Architecture + MVVM
*   **DI:** Hilt (Dagger)
*   **Database:** Room
*   **Networking:** Retrofit + OkHttp
*   **Background Tasks:** WorkManager
*   **Local Preferences:** Jetpack DataStore

---

## 📂 Project Structure & Navigation
*   **`domain`:** Core business entities and Use Cases.
*   **`data`:** Repository implementations, Room DB, Retrofit API, and WorkManager.
*   **`presentation`:** Compose UI screens and ViewModels.
*   **Navigation:** Managed via a centralized `NavHost` with 4 core screens (Home, Add Expense, Analysis, Settings).

---

## 🏗 Developer Guide

### 🚀 How to Launch in Android Studio
1. **Open the Project:** Launch Android Studio and click **Open**. Select the `Spendly` root folder.
2. **Gradle Sync:** Wait for Android Studio to finish the initial Gradle sync (it will download necessary dependencies like Compose, Hilt, Room, etc.).
3. **Select a Device:** Choose an Android Virtual Device (Emulator) or a physical device connected via USB/Wi-Fi from the device dropdown menu in the top toolbar.
4. **Run the App:** Click the green **Run 'app'** button (or press `Shift + F10`). The IDE will build the project and launch it on your selected device.

For a detailed list of **Completed Work** and **Remaining Implementation Tasks**, please refer to the internal status tracker:

👉 **[PROJECT_STATUS.md](./PROJECT_STATUS.md)**

---

## 🎓 Evaluation Checklist
- [x] Native Android (Kotlin + Compose)
- [x] Architectural Separation (Clean + MVVM)
- [x] Local Persistence (Room)
- [x] REST Integration (Retrofit GET/POST)
- [x] Asynchronous Processing (Coroutines + Flow)
- [x] Offline-First Strategy (Connectivity Monitoring)
- [x] Meaningful Sensor Use (Location tagging)
- [x] Bonus: NFC Integration scaffolded
