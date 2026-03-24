# Spendly - Presentation & Defense Guide

This document provides all the technical justifications, architectural details, and use-case flows required for the Spendly project presentation and defense (Week 7).

---

## 1. Project Concept & Problem Definition
**Concept:** Spendly is a high-performance, offline-first personal finance manager.
**Problem:** Most expense trackers fail in low-connectivity areas or require too much manual entry.
**Solution:** A robust Android system that uses **Location Sensors** to auto-tag entries, **NFC** for instant "Tap-to-Log" actions, and a **Sync Engine** that guarantees data integrity across local and cloud storage.

---

## 2. Technical Stack (Requirement 3.1)
- **Language:** Kotlin (leveraging Coroutines for non-blocking UI).
- **UI:** Jetpack Compose (Material 3) for a modern, declarative interface.
- **DI:** Hilt (Dagger) for scalable dependency management.
- **Database:** Room (SQLite) as the Single Source of Truth.
- **Networking:** Retrofit + OkHttp for REST communication with Firebase.

---

## 3. Use Case Flows (Defense Walkthrough)

### Use Case A: Logging a Contextual Expense
1. **Trigger:** User opens "Add Expense" screen.
2. **Sensor Action:** App requests location permission and uses `FusedLocationProviderClient`.
3. **Data Enrichment:** `Geocoder` converts coordinates to a city name (e.g., "Porto") and auto-fills the location field.
4. **Hardware Shortcut:** If the user makes a mistake, they **Shake the Device** (Accelerometer); the app detects the motion and clears the form.
5. **Persistence:** Saving writes to **Room** instantly (UI updates via Flow).

### Use Case B: NFC "Quick Log" (Bonus Task)
1. **Trigger:** User taps a physical NFC tag (e.g., on a coffee machine) while the app is in the background or foreground.
2. **Processing:** `MainActivity` intercepts the `NDEF_DISCOVERED` intent.
3. **Action:** `ProcessNfcTagUseCase` parses the tag payload (e.g., "1.50|Coffee|Food") and saves it to the database automatically.

### Use Case C: The Offline-to-Online Sync (Requirement 3.7)
1. **Scenario:** User logs an expense while in a subway (No Internet).
2. **Local State:** Entry is saved to Room with `isSynced = false`.
3. **UI Feedback:** A global "Offline Mode" banner appears.
4. **Recovery:** Once internet is restored, `ConnectivityObserver` triggers a signal.
5. **Action:** `MainViewModel` instantly calls `syncWithRemote()`, pushing all pending entries to Firebase.

---

## 4. Architectural Justification (Requirement 3.8)
We utilized **Clean Architecture** with a clear 3-layer separation:
1. **Data Layer:** (Room + Retrofit) Encapsulates data sourcing logic.
2. **Domain Layer:** (Use Cases) Contains pure business logic, making it independent of UI or Frameworks.
3. **Presentation Layer:** (MVVM) ViewModels expose an immutable `UiState`. The UI is "dumb" and only reacts to state changes.

**Why this matters:** This separation ensures the app is highly testable and allows multiple developers to work on different features (UI vs. API) without merge conflicts.

---

## 5. Defense FAQ - Key Technical Decisions

**Q: Why use Room if you have Firebase?**
*A: To fulfill the Offline-First requirement. Room is our Single Source of Truth. The UI never talks to the internet; it only talks to the database. This ensures 100% usability offline.*

**Q: How did you ensure the UI stays responsive?**
*A: Every database and network operation is wrapped in a Kotlin Coroutine (`Dispatchers.IO`) handled by the Repository. We use `StateFlow` to stream updates to the Compose UI.*

**Q: How does the location sensor "influence behavior"?**
*A: It transforms a manual task (typing "Porto") into an automated one. This fulfills Requirement 3.6 by making the sensor functionally relevant to the core use case.*

**Q: What is the "Degraded Behavior" when offline?**
*A: The "Manual Sync" button in settings is disabled and the label changes to "Sync unavailable," preventing user frustration from failed actions.*

---

## 6. Demonstration Scenario (The "Perfect Demo")
1. **Open App:** Show the Dashboard with existing data.
2. **Go Offline:** (Turn on Airplane Mode). Show the "Offline Mode" banner.
3. **Add Entry:** Add a "5.00€ Lunch" entry. Show that it appears in the list immediately despite being offline.
4. **Go Online:** (Turn off Airplane Mode).
5. **The Magic Moment:** Watch the banner disappear and explain that the app is now automatically pushing the "Lunch" entry to the cloud in the background.
6. **Charts:** Navigate to "Analysis" to show the spending breakdown.
7. **NFC (If possible):** Tap a tag to show a "Quick Log" entry appearing.
