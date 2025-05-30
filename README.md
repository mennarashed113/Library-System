# Library Management System

## Overview

This Library Management System is a robust, secure, and extensible backend API built with Java, Spring Boot, and MySQL. It allows administrators, librarians, and staff to manage books, members, and borrowing transactions with role-based access control and comprehensive logging.

---

## Technology Stack

* *Java 21*: Modern LTS version, strong typing, stability.
* *Spring Boot 3.x*: Rapid application setup, embedded server, auto-configuration.
* *Spring Data JPA (Hibernate)*: Simplifies ORM and database interactions.
* *Spring Security*: HTTP Basic authentication with role-based authorization.
* *MySQL 8*: Relational database for reliable data persistence.
* *Lombok*: Reduces boilerplate in entity classes.
* *Maven*: Dependency management and build lifecycle.

---

## Design Choices

### 1. *Layered Architecture*

* *Controllers*: Handle HTTP requests and map to service calls.
* *Services*: Contain business logic (e.g., borrowing limits, return process).
* *Repositories*: Leverage Spring Data JPA for CRUD operations.
* *Entities*: Annotated with JPA annotations to define table mappings and relationships.

### 2. *Entity Relationships*

* *Book ↔ Author*: Many-to-many via join table to support multiple authors per book.
* *Book ↔ Category*: Many-to-many with hierarchical categories (parent-child) for flexible genre classification.
* *Book ↔ Publisher*: Many-to-one to capture publisher metadata.
* *Member ↔ BorrowingTransaction*: One-to-many for member borrowing history.
* *Book ↔ BorrowingTransaction*: One-to-many to track each borrowing event.
* *SystemUser*: Single table with role enum for ADMIN, LIBRARIAN, STAFF, MEMBER.

### 3. *Security & Authentication*

* *BCryptPasswordEncoder* for secure password storage.
* *CustomUserDetailsService* to load user credentials from DB.
* *Role-based Authorization* using .authorizeHttpRequests() to protect endpoints.
* *PermitAll* on /api/users/register & /api/users/login for onboarding.

### 4. *Borrowing Logic*

* *Max 3 books per member* enforced in service/controller.
* *Single-copy enforcement*: Prevent borrowing if returnDate is NULL on active transaction.
* *Return endpoint* marks returnDate to free the book.

### 5. *Logging & Auditing*

* *Console logs* for user registration and login events using System.out.println.
* Easily extendable to database-based audit logs for production.

---

## Setup & Run

1. *Install Java 21, **MySQL 8, and **Maven*.
2. Create MySQL user and database:

   sql
   CREATE DATABASE library_db;
   CREATE USER 'library_user'@'localhost' IDENTIFIED BY 'libpass';
   GRANT ALL PRIVILEGES ON library_db.* TO 'library_user'@'localhost';
   FLUSH PRIVILEGES;
   
3. Configure src/main/resources/application.properties:

   properties
   
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3307/library_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate Properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# Optional: Disable feature that might cause issues
spring.jpa.open-in-view=false
   
4. Build and run:

   bash
   mvn clean install
   mvn spring-boot:run
   

---

## API Endpoints

### Authentication

* POST /api/users/register — Register new user
* POST /api/users/login — Login (HTTP Basic Auth)

### Books

* GET /api/books — List all books
* GET /api/books/{id} — Get book by ID
* POST /api/books — Create book (ADMIN, LIBRARIAN, STAFF)
* PUT /api/books/{id} — Update book
* DELETE /api/books/{id} — Delete book

### Members

* GET /api/members — List members (ADMIN, LIBRARIAN)
* GET /api/members/{id} — Get member (ADMIN, LIBRARIAN)
* POST /api/members — Create member
* PUT /api/members/{id} — Update member
* DELETE /api/members/{id} — Delete member

### Borrowings

* POST /api/borrowings?memberId=&bookId= — Borrow book
* POST /api/borrowings/return?memberId=&bookId= — Return book
* GET /api/borrowings — List all borrowings
* GET /api/borrowings/{id} — Get transaction by ID
