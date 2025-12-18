# ⚖️ equalize – Intelligent Team Formation System

![Java](https://img.shields.io/badge/Java-21-blue?logo=java)
![JavaFX](https://img.shields.io/badge/JavaFX-UI-green)
![Build](https://img.shields.io/badge/Build-Maven-orange)
![Status](https://img.shields.io/badge/Status-Completed-brightgreen)
![Last Updated](https://img.shields.io/badge/Last%20Updated-November%202025-purple)

> **equalize** intelligently forms balanced teams using participant **personality traits, preferred roles, games, and skill levels** — replacing random grouping with data-driven fairness.

---

## 🧭 Overview

**Equalize** is a JavaFX-based desktop application developed for an **Object-Oriented Programming (OOP)** coursework project.  
The system supports **participants** and **organizers**, allowing structured data collection, validation, preprocessing, and intelligent team formation.

The goal of Equalize is simple:

> 🎯 *Create fair, balanced teams by equalizing skills, roles, and personalities.*

---

## 👥 User Roles

### 👤 Participant
Participants can:
- Create an account with validation (ID & university email)
- Log in securely
- Complete a **5-question personality survey**
- Select:
  - Preferred team role
  - Preferred game
  - Skill level (1–10)
- Review submitted details

### 🧑‍💼 Organizer
Organizers can:
- Load participant data from CSV
- Validate and preprocess records
- View invalid rows with detailed error messages
- Form balanced teams based on constraints
- View teams and members
- Export formed teams to CSV

---

## ⚙️ Core Features

- 🧠 **Personality Scoring System**  
  Calculates and classifies participants into:
  - LEADER
  - BALANCED
  - THINKER
  - NEEDS_REVIEW

- 📊 **Validation & Error Handling**
  - Duplicate ID/email detection
  - Strict input validation
  - Clear user-facing error messages

- 🧵 **Concurrency Support**
  - Background processing of participant data
  - Thread pool management for scalability

- 📁 **CSV-Based Persistence**
  - Load, validate, and store participant data
  - Export final team compositions

- 🖥️ **Modern JavaFX UI**
  - Styled with CSS
  - Clean navigation via RootLayout
  - Alerts, confirmations, and dialogs

- 🧪 **Unit Testing (JUnit 5)**
  - Service-level tests
  - CSV repository tests
  - Validation logic tests

---

## 🏗️ System Architecture

```
UI (JavaFX)
   ↓
Controllers
   ↓
Services
   ↓
Repositories
   ↓
CSV Files
```

---

## 🚀 Running the Application

### Prerequisites
- **Java JDK 21**
- **Maven**

```bash
mvn clean javafx:run
```

---

## 🧪 Testing

```bash
mvn test
```

---

## 🧑‍💻 Author

**Equalize – Intelligent Team Formation System**  
Developed by **Senyaka**  


---

⭐ Star this repo if you support fair and intelligent team formation!
