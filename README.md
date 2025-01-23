### Book Borrowing System - Desktop Application

---

## Table of Contents
1. [Introduction](#introduction)
2. [Features](#features)
3. [Technologies Used](#technologies-used)
4. [Installation](#installation)
5. [Usage](#usage)
6. [Screenshots](#screenshots)
7. [Database Schema](#database-schema)
8. [Project Structure](#project-structure)
9. [Contributors](#contributors)
10. [License](#license)
11. [Contact](#contact)

---

## Introduction

This project is a **Book Borrowing System** developed as a **college project** to manage library operations efficiently. It is a **desktop application** built using **Java** for the backend logic and **Java Swing** for the user interface. The system integrates with a **MySQL database** to store and manage data related to books, students, and users.

The application provides a comprehensive set of features for librarians to manage books, issue and return books, manage student information, and handle user accounts. It is designed to streamline library operations and improve the overall management process.

---

## Features

### 1. **User Management**
   - **Add, delete, and update user accounts**.
   - **Validate user credentials** during login.
   - Secure password management.

### 2. **Student Management**
   - **Add, delete, and update student information**.
   - **Search for students by name**.
   - Manage student details such as course, branch, and semester.

### 3. **Book Management**
   - **Add, delete, and update book details**.
   - **Issue books to students** and track due dates.
   - **Return books** and update their availability status.
   - **Search for books by name**.
   - **View issued books** and **overdue books**.

### 4. **Database Integration**
   - Uses **MySQL** for data storage.
   - Supports **CRUD operations** for books, students, and users.
   - Efficiently manages relationships between books and students.

---

## Technologies Used

- **Programming Language**: Java
- **User Interface**: Java Swing
- **Database**: MySQL
- **Database Connectivity**: JDBC (Java Database Connectivity)
- **Version Control**: Git

---

## Installation

### Prerequisites

1. **Java Development Kit (JDK)**: Ensure JDK 8 or higher is installed.
2. **MySQL Server**: Install MySQL and set up a database named `javalibrary`.
3. **MySQL Connector/J**: Download the MySQL JDBC driver and add it to your project's classpath.

### Steps to Run the Project

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Ziad-Abaza/Book-Borrowing-System-Desktop.git
   ```

2. **Set Up the Database**:
   - Run the provided SQL script (`database.sql`) to create the necessary tables in your MySQL database.
   - Update the `DatabaseConnection` class with your MySQL credentials (URL, username, password).

3. **Import the Project**:
   - Open the project in your preferred Java IDE (e.g., IntelliJ IDEA, Eclipse).

4. **Run the Application**:
   - Locate the `LoginUI` class in the `library.management.system.ui` package.
   - Run the `LoginUI` class to start the application.

5. **Login Credentials**:
   - Use the following default credentials to log in (or create new users via the UI):
     - **User ID**: `admin`
     - **Password**: `admin`

---

## Usage

1. **Login**:
   - Enter your username and password to access the system.

2. **Book Management**:
   - Add new books, update existing ones, or delete books.
   - Issue books to students and return them when they are done.

3. **Student Management**:
   - Add, update, or delete student information.
   - Search for students by name.

4. **User Management**:
   - Add, delete, or update user accounts.
   - Change user passwords.

5. **Reports**:
   - View a list of issued books.
   - View a list of students with overdue books.

---

## Screenshots

### Login Page
![Login Page](https://github.com/Ziad-Abaza/Book-Borrowing-System-Desktop/blob/main/login.png)

### Book Management Page
![Book Management](https://github.com/Ziad-Abaza/Book-Borrowing-System-Desktop/blob/main/book.png)

### Student Management Page
![Student Management](https://github.com/Ziad-Abaza/Book-Borrowing-System-Desktop/blob/main/student.png)

### User Management Page
![Issue Book]()

---

## Database Schema

The database consists of the following tables:

1. **`book` Table**:
   - Stores book details such as name, publisher, price, status, issue date, due date, and student ID (if issued).

2. **`student` Table**:
   - Stores student details such as name, course, branch, and semester.

3. **`login` Table**:
   - Stores user credentials (user ID and password).

### SQL Script

```sql
CREATE TABLE `book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `publisher` varchar(100) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `status` enum('available','issued') DEFAULT 'available',
  `issuedate` date DEFAULT NULL,
  `duedate` date DEFAULT NULL,
  `studentid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `studentid` (`studentid`),
  CONSTRAINT `book_ibfk_1` FOREIGN KEY (`studentid`) REFERENCES `student` (`id`)
);

CREATE TABLE `login` (
  `userid` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`userid`)
);

CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `course` varchar(100) NOT NULL,
  `branch` varchar(50) NOT NULL,
  `semester` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
);
```

---

## Project Structure

```
Book-Borrowing-System-Desktop/
├── src/
│   ├── library/
│   │   ├── management/
│   │   │   ├── system/
│   │   │   │   ├── dao/              # Data Access Objects (DAO)
│   │   │   │   ├── model/            # Model Classes (User, Student, Book)
│   │   │   │   ├── ui/               # User Interface Classes
│   │   │   │   └── DatabaseConnection.java
│   │   │   └── ...
│   │   └── ...
│   └── ...
├── screenshots/                      # Screenshots of the application
├── README.md                         # Project documentation
└── database.sql                      # SQL script for database setup
```

---

## Contributors

- **Ziad Abaza** - Project Developer
- **College Name** - Borg El Arab Technological University

---

## License

This project is licensed under the **MIT License**. Feel free to use, modify, and distribute it as per the license terms.

---

## Contact

For any queries or feedback, please contact:
- **Email**: ziad.abaza@example.com
- **GitHub**: [Ziad-Abaza](https://github.com/Ziad-Abaza)

---

Thank you for checking out the **Book Borrowing System**! We hope this project serves as a useful resource for your college studies and beyond. 🚀
