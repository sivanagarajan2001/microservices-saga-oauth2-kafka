# Kafka Saga Microservices

## 🚀 Project Overview

This project demonstrates a **Microservices Architecture** using **Spring Boot** and **Apache Kafka** with the **Saga Pattern (Choreography-based)** to handle distributed transactions.

The system consists of multiple independent services that communicate asynchronously using Kafka events. It ensures **fault tolerance**, **loose coupling**, and **eventual consistency** across services.

---

## 🧱 Services

- **Order Service**
  - Creates orders
  - Publishes `ORDER_CREATED` event
  - Updates final order status

- **Payment Service**
  - Consumes order events
  - Processes payment
  - Publishes `PAYMENT_SUCCESS` / `PAYMENT_FAILED`

- **Transaction Service**
  - Consumes payment events
  - Performs transaction validation
  - Publishes `TRANSACTION_SUCCESS` / `TRANSACTION_FAILED`

---

## 🔄 Event Flow

1. Order Service creates an order → sends `ORDER_CREATED`
2. Payment Service processes payment:
   - Success → `PAYMENT_SUCCESS`
   - Failure → `PAYMENT_FAILED`
3. Transaction Service processes result:
   - Success → `TRANSACTION_SUCCESS`
   - Failure → `TRANSACTION_FAILED`
4. Order Service updates final status:
   - `SUCCESS` → if all steps succeed
   - `PENDING` → if any step fails (rollback behavior)

---

## 🔁 Saga Pattern (Distributed Transaction)

- Uses **Kafka-based event choreography**
- No direct service-to-service API calls
- Each service reacts to events
- Rollback handled via failure events

---

## ⚙️ Tech Stack

- Spring Boot  
- Spring Kafka  
- Maven  
- MySQL (for Order Service)  
- Apache Kafka  

---

## 🏗️ Architecture
