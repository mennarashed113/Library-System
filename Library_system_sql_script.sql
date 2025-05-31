CREATE TABLE author (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE book (
  id bigint NOT NULL AUTO_INCREMENT,
  cover_image_url varchar(255) DEFAULT NULL,
  edition varchar(255) DEFAULT NULL,
  isbn varchar(255) DEFAULT NULL,
  language varchar(255) DEFAULT NULL,
  publication_year int DEFAULT NULL,
  summary varchar(1000) DEFAULT NULL,
  title varchar(255) DEFAULT NULL,
  publisher_id bigint NOT NULL,
  PRIMARY KEY (id),
  KEY FKgtvt7p649s4x80y6f4842pnfq (publisher_id),
  CONSTRAINT FKgtvt7p649s4x80y6f4842pnfq FOREIGN KEY (publisher_id) REFERENCES publisher (id)
);

CREATE TABLE book (
CREATE TABLE category (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  parent_id bigint DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK2y94svpmqttx80mshyny85wqr (parent_id),
  CONSTRAINT FK2y94svpmqttx80mshyny85wqr FOREIGN KEY (parent_id) REFERENCES category (id)
);

CREATE TABLE user (
  id bigint NOT NULL AUTO_INCREMENT,
  full_name varchar(255) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  role enum('ADMIN','LIBRARIAN','STAFF') DEFAULT NULL,
  username varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE publisher (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UKh9trv4xhmh6s68vbw9ba6to70 (name)
);

CREATE TABLE member (
  id bigint NOT NULL AUTO_INCREMENT,
  address varchar(255) DEFAULT NULL,
  email varchar(255) DEFAULT NULL,
  full_name varchar(255) DEFAULT NULL,
  phone varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

INSERT INTO system_user (username, password, role)
VALUES 
('admin1', '$2a$10$7s3T09J7OezViYw.PoJszOYIUy5Shq2WPyZsffvvUF52gDwUyWT8S', 'ADMIN'),  -- password: admin123
('librarian1', '$2a$10$3jHv.X4owgGHUePG3M1jkOXRlAFLP.rZfGJkQkmtRRGYc0wheLRZ6', 'LIBRARIAN'),  -- password: lib123
('staff1', '$2a$10$FUGZLSdN8IfpRMydxM6KDezYcZy1eEEMMiKZJcbGHGyIxAyhYok2q', 'STAFF');  -- password: staff123


INSERT INTO member (name, email, phone, address)
VALUES 
('John Doe', 'john@example.com', '1234567890', '123 Street'),
('Jane Smith', 'jane@example.com', '0987654321', '456 Avenue');


INSERT INTO publisher (name)
VALUES 
('Penguin Random House'),
('HarperCollins');


INSERT INTO category (name)
VALUES 
('Fiction'),
('Science'),
('Technology');


INSERT INTO author (name)
VALUES 
('George Orwell'),
('Isaac Asimov');


INSERT INTO book (title, language, publication_year, isbn, edition, summary, cover_image, publisher_id, category_id)
VALUES 
('1984', 'English', 1949, '978-0451524935', '1st', 'Dystopian novel', 'cover1.jpg', 1, 1),
('Foundation', 'English', 1951, '978-0553293357', '1st', 'Sci-fi classic', 'cover2.jpg', 2, 2);


INSERT INTO book_authors (book_id, author_id)
VALUES 
(4, 1),
(6, 2);


INSERT INTO borrowing_transaction (book_id, member_id, borrow_date, due_date, return_date)
VALUES 
(1, 1, '2024-01-01', '2024-01-14', NULL),
(2, 2, '2024-01-05', '2024-01-20', '2024-01-18');
