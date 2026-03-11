Mobile Computing

Lab Assignment 1

Android Native Application

1. Introduction

Lab Assignment 1 consists of the design and development of a native Android application using Kotlin and Jetpack Compose.

Students are free to define the application domain and concept. This flexibility is intentional: the objective is not to replicate a fixed scenario, but to design and implement a mobile system that demonstrates architectural maturity, integration capabilities, and correct use of the Android platform.

The assignment focuses on building a medium-sized, architecturally sound mobile system, developed over approximately six weeks, capable of integrating local persistence, remote services, asynchronous processing, and device-level interaction through sensors.

The goal is not complexity for its own sake, but coherence, robustness, and architectural clarity.

Students must form teams of three members during the first week of classes. Team composition is fixed after submission. All members are expected to contribute to major tasks, such as :

● Requirements definition

● Architectural design

● Implementation

● Testing

● Presentation and demonstration

The assignment is evaluated at the team level.

2. Project Scope

Each team is free to define the application’s: Domain and context; Target users; Functional features; Interaction model; and Visual identity.

Possible examples include (but are not limited to):

● Smart activity tracker

● Event management platform

● Field reporting system

CMOV – Rui Pinto & Konstantinos Bletsas

1 | Page ● Asset tracking application

● Personal productivity assistant

● IoT companion mobile client

● Service booking application

Creativity is encouraged. However, all projects must comply with the mandatory technical and architectural requirements described below.

3. Technical Requirements

All projects must satisfy the following mandatory criteria.

3.1 Platform and Technologies

The application must:

Be developed as a native Android application Use Kotlin as the programming language Use Jetpack Compose for the user interface Use Android Studio and Gradle

3.2 User Interface and Navigation

The application must include:

At least four distinct screens Structured navigation using Compose navigation Proper state handling Clear separation between UI and logic

Navigation logic must be well-defined and coherent with the application’s use cases.

3.3 Local Data Management

The application must persist structured data locally. This implies:

Definition of a data model Use of a persistent local storage mechanism Ability to restart the application without losing core data Demonstration of offline usability for essential features

Data should not exist exclusively in memory.

3.4 Remote Service Integration

The application must integrate with at least one REST/HTTP service. The system must perform:

At least one GET request

CMOV – Rui Pinto & Konstantinos Bletsas

2 | Page At least one POST or PUT request

Remote communication must:

Be asynchronous Avoid blocking the UI thread Handle errors gracefully Display appropriate loading states

The backend may be:

A public API A lightweight custom backend A mock server implementation

However, the networking layer must be clearly separated from the presentation layer.

3.5 Asynchronous Processing

The application must demonstrate correct use of asynchronous execution, such as:

Kotlin coroutines Proper handling of background operations Clear management of loading and error states

The UI must remain responsive during network operations.

3.6 Device Integration

Each project must meaningfully integrate at least one Android sensor. Examples include:

● Location sensor

● Accelerometer

● Gyroscope

● Light sensor

● Proximity sensor

● Motion sensors

Sensor data must influence application behavior. It cannot be used merely for display without functional relevance.

As a bonus, if you have an available, compatible physical Android device, you may use NFC to:

● Read tags

● Trigger actions

● Associate physical objects with digital entities

3.7 Offline Behavior

The application must demonstrate awareness of connectivity constraints. Specifically, it must:

CMOV – Rui Pinto & Konstantinos Bletsas

3 | Page Continue functioning when internet connectivity is lost Cache remote data locally Provide degraded but usable behavior offline Synchronize data when connectivity is restored

Offline-first design principles will be positively evaluated.

3.8 Architectural Organization

Projects must exhibit clear architectural separation between:

● Presentation layer

● Application/domain logic

● Data layer (local + remote sources)

The architecture does not need to strictly follow a specific named pattern, but it must be coherent and explainable.

4. Deliverables

The following must be submitted:

● Source code repository

● APK file

● Fully functional application

● Slide deck used during the presentation. The slides must include:

○ Application concept and problem definition ○ Requirements overview ○ UI wireframes and navigation structure ○ Data model description ○ Architectural overview ○ Description of REST integration ○ Offline strategy ○ Sensor integration explanation ○ Demonstration scenario

● Small demo video (maximum 2 minutes)

5. Presentation and Demonstration

The final presentation will take place in Week 7 (8 April 2026) during class. Each team will have 10 minutes for demonstration + 5 minutes for questions and discussion.

Presentations must focus on: i) Functional walkthrough; ii) Architectural explanation; iii) Technical justification; iv) Design decisions.

Live demonstration is mandatory!

CMOV – Rui Pinto & Konstantinos Bletsas

4 | Page 6. Evaluation Criteria

Evaluation will consider:

● Architectural quality and separation of concerns

● Correct use of Kotlin and Jetpack Compose

● Quality of local data management

● Correct asynchronous processing

● Robust REST integration

● Meaningful sensor integration

● Offline capability

● Code organization and clarity

● Technical justification during presentation

● Overall coherence and usability

Visual design alone will not determine the grade.

7. Final Remarks

This assignment is designed to simulate the development of a realistic mobile system operating under practical and technical constraints. Throughout the project, you will need to consider connectivity variability, device limitations, architectural organization, and the integration of multiple data sources. These constraints are not incidental; they are inherent characteristics of mobile computing and should inform your design decisions from the outset.

The primary objective of this assignment is not the quantity of code produced, but the quality of the architectural reasoning behind it. A coherent, well-structured, and thoughtfully designed medium-sized system will be evaluated more positively than a larger application that lacks separation of concerns, robustness, or clear design principles. The emphasis is on demonstrating architectural maturity, sound engineering judgment, and the ability to justify technical decisions.

CMOV – Rui Pinto & Konstantinos Bletsas

5 | Page