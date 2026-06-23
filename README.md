# 💳 Digital Wallet API

> Fintech-grade Digital Wallet REST API built with Java 21 & Spring Boot 3

A production-inspired digital wallet system demonstrating modern backend engineering practices — Clean Architecture, Domain-Driven Design, and event-driven patterns.

---

## 🏗️ Architecture

This project follows **Clean Architecture** with **DDD** principles, organized in four layers.

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.4 |
| Build | Maven |
| Persistence | Spring Data JPA + H2 (dev) |
| Validation | Jakarta Validation |
| Boilerplate | Lombok |
| Observability | Spring Boot Actuator |

## 📡 API Endpoints

### Users
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/users` | Register a new user |

### Wallets
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/wallets` | Create wallet for a user |
| GET | `/api/v1/wallets/users/{userId}` | Get wallet and balance |

### Transactions
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/transactions/deposit` | Deposit funds |
| POST | `/api/v1/transactions/withdrawal` | Withdraw funds |
| GET | `/api/v1/transactions/users/{userId}` | Transaction history |

### Transfers
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/transfers` | Transfer between wallets |

## 📦 Features

- [x] User registration & management
- [x] Wallet creation per user
- [x] Deposit & Withdraw operations
- [x] Wallet-to-wallet transfers
- [x] Transaction history
- [x] Balance inquiry
- [x] Input validation & RFC 7807 error handling
- [x] OpenAPI / Swagger documentation

---

> Built by [João](https://github.com/joaodddev) · Portfolio project · fintech-inspired