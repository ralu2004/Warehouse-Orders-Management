#🏷️ Warehouse Orders Management System
A full-featured Java application for managing client orders in a warehouse, built using JavaFX, PostgreSQL, Java Reflection, and Maven. This project demonstrates advanced use of OOP, layered architecture, custom ORM, and modern functional programming techniques.

##🎯 Features
Client Management: Add, edit, delete, and view clients

Product Management: Add, edit, delete, and view products

Order Processing:

Create orders by selecting clients and products

Validate stock before processing

Automatically decrement stock on successful order

Immutable Bills:

Auto-generated bill (record) for each order

Stored in a Log table (immutable via database triggers)

Dynamic Table Generation: GUI tables auto-generate headers and content using reflection

Generic Data Access Layer: ORM-style database operations using Java reflection and custom annotations

Validation & Error Handling: Business logic validators and custom exceptions for meaningful feedback

##🧠 Architecture & Design
The application follows a clean Layered Architecture:

model — data models (POJOs & record classes)

businessLayer — core business logic and validation

dataAccessLayer — generic DAO with reflection-based ORM

presentation — JavaFX GUI: controllers, views, user interaction

GUI follows a partial MVC pattern:

Model (domain objects)

View (FXML UI)

Controller (UI logic and event handling)

##🔧 Technologies Used
Java 17+

JavaFX

PostgreSQL

Maven

JDBC

Java Reflection

Java Records

##🧱 OOP Principles
Encapsulation: Separation of concerns across layers

Abstraction: Interfaces and clean public APIs

Inheritance: Reusable base components

Polymorphism: Method overloading and dynamic behavior

##📐 Design Patterns
Singleton — database connection management

DAO (Data Access Object) — abstract DB access via a generic DAO class

MVC (Model-View-Controller) — partial application for GUI structure

Template Method — reused logic in reflection-based DAO

##🧬 Functional Programming
Modern Java features are used extensively:

Lambda expressions for concise callbacks and logic

Streams API for processing and transforming collections

Immutability through Java record types (e.g., Bill)

Method references and fluent programming style

##🧪 Reflection-Based ORM
A lightweight custom ORM layer using Java Reflection and annotations.

@Table and @Column define mapping to database schema

Generic DAO generates SQL dynamically at runtime

Supports CRUD operations across all entity types

##🖥️ GUI Overview
JavaFX multi-window application

Tables are generated dynamically based on object structure

Interfaces for:

Clients

Products

Orders

Error messages displayed on invalid input or under-stock

##🛠️ Setup Instructions
Clone the repository:
git clone https://github.com/your-username/warehouse-orders.git
Configure PostgreSQL:
Create a database: database_name
Update DB connection in your connection utility:
url = "jdbc:postgresql://localhost:5432/ordersdb"
user = "your_username"
password = "your_password"
Run the database dump script in your PostgreSQL console to get going.

Build and run the application:
Open in IntelliJ
Use Maven to build
Run View.java

##📃 License
This project is licensed under the MIT License.

##👤 Author
Your Name
GitHub: https://github.com/ralu2004

