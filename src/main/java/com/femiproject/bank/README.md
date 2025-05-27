# 💳 CLI Banking System

A simple command-line interface (CLI) banking application built in **Java**, supporting basic banking operations like **account creation**, **login**, **deposit**, **withdrawal**, **transfer**, and **transaction history**. The application uses **virtual threads (Project Loom)** for scalable and efficient concurrency, and persists data in a JSON file using **Jackson**.

---

## 🚀 Features

- ✅ User registration and authentication
- 💰 Deposit, withdraw, and transfer functionality
- 🧾 Transaction history tracking
- 🏦 Balance inquiry
- 💾 Persistent storage using JSON
- ⚡ Efficient and lightweight concurrency using **virtual threads**
- ✅ Unit-tested using **JUnit 5**
- 👥 Simulation support for 200 users performing transactions concurrently

---

## 🛠️ Technologies Used

- **Java 21**
- **Virtual Threads (Project Loom)**
- **Jackson (JSON parsing and serialization)**
- **JUnit 5** for testing
- **Maven** (or Gradle) for dependency management

---

## 🧠 Architecture Overview

The application is structured into three main components:

1. **`Main` (CLI interface)**:

   - Handles user input/output
   - Manages navigation through the banking menu
   - Delegates operations to `BankSystem`

2. **`BankSystem` (Core logic)**:

   - Manages users, balances, and transactions
   - Uses `ExecutorService` with `Executors.newVirtualThreadPerTaskExecutor()` to handle all user operations in lightweight threads
   - Thread-safe operations using synchronized blocks
   - Persists all users to a `users.json` file

3. **`User` and `Transaction` classes**:
   - Represent user accounts and their transaction history
   - Each transaction is timestamped and categorized (deposit, withdraw, transfer)

---

## 🧵 Virtual Threads in Action

The system leverages Java’s **virtual threads** to support high-concurrency transaction handling. Here's how it works:

```java
this.service = Executors.newVirtualThreadPerTaskExecutor();
```
