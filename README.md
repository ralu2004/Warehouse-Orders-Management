# 🏷️ Warehouse Orders Management System

A full-featured Java application for managing client orders in a warehouse, built using JavaFX, PostgreSQL, Java Reflection, and Maven. This project demonstrates advanced use of OOP, layered architecture, custom ORM, and modern functional programming techniques.

---

## 🎯 Features

- **Client Management**: Add, edit, delete, and view clients  
- **Product Management**: Add, edit, delete, and view products  
- **Order Processing**:
  - Create orders by selecting clients and products
  - Validate stock before processing
  - Automatically decrement stock on successful order  
- **Immutable Bills**:
  - Auto-generated bill (record) for each order
  - Stored in a Log table (immutable via database triggers)
- **Dynamic Table Generation**: GUI tables auto-generate headers and content using reflection
- **Generic Data Access Layer**: ORM-style operations using reflection + custom annotations
- **Validation & Error Handling**: Business logic validators and custom exceptions

---

## 🧠 Architecture & Design

The application follows a clean **Layered Architecture**:

- `model` — data models (POJOs & record classes)  
- `businessLayer` — core business logic and validation  
- `dataAccessLayer` — generic DAO with reflection-based ORM  
- `presentation` — JavaFX GUI: controllers, views, user interaction  

GUI uses a partial MVC pattern:

- **Model** — domain objects  
- **View** — FXML UI  
- **Controller** — UI logic and event handling  

---

## 🔧 Technologies Used

- Java 17+  
- JavaFX  
- PostgreSQL  
- Maven  
- JDBC  
- Java Reflection  
- Java Records  

---

## 🧱 OOP Principles

- **Encapsulation**: Separation of concerns across layers  
- **Abstraction**: Interfaces and clean public APIs  
- **Inheritance**: Reusable base components  
- **Polymorphism**: Method overloading and dynamic behavior  

---

## 📐 Design Patterns

- **Singleton** — database connection management  
- **DAO** (Data Access Object) — abstract DB access via generic DAO  
- **MVC** — for GUI structure  
- **Template Method** — for reusable logic in the DAO  

---

## 🧬 Functional Programming

Modern Java features include:

- Lambda expressions for concise logic  
- Streams API for collection processing  
- Immutability via `record` types  
- Method references and fluent style  

---

## 🧪 Reflection-Based ORM

A lightweight ORM layer using Java Reflection + annotations:

- `@Table` and `@Column` map to the DB schema  
- Generic DAO generates SQL dynamically at runtime  
- CRUD operations across all entities  

---

## 🖥️ GUI Overview

- JavaFX multi-window application  
- Tables are generated dynamically using reflection  
- Interfaces for:
  - Clients
  - Products
  - Orders  
- Real-time error messages for invalid input or stock issues  

---

## 🛠️ Setup Instructions

**Clone the repository:**

```bash
git clone https://github.com/your-username/warehouse-orders-management.git
```

### 2. Configure PostgreSQL

**Create the database:**

```sql
CREATE DATABASE ordersdb;
```

** Define connection parameters:**

```java
String url = "jdbc:postgresql://localhost:5432/ordersdb";
String user = "your_username";
String password = "your_password";
```

**Initialize the database schema using the provided SQL dump:**

```bash
psql -U your_username -d ordersdb -f path/to/init.sql
```

**Build and Run the Application**

```bash
mvn clean install
```

##  License

This project is licensed under the MIT License.

## Author

Raluca Adam  
GitHub: [@ralu2004](https://github.com/ralu2004)

