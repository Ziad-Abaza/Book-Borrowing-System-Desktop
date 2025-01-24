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

This project is a **Book Borrowing System** developed as a **college project** to manage library operations efficiently. It is a **desktop application** built using **Java** for the backend logic and **Java Swing** for the user interface. The system integrates with a **SQLite database** to store and manage data related to books, students, and users.

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
   - Uses **SQLite** for data storage.
   - Supports **CRUD operations** for books, students, and users.
   - Efficiently manages relationships between books and students.

---

## Technologies Used

- **Programming Language**: Java
- **User Interface**: Java Swing
- **Database**: SQLite
- **Database Connectivity**: JDBC (Java Database Connectivity)
- **Libraries/Packages**:
  - `sqlite-jdbc-3.48.0.0.jar` (Located in `Book-Borrowing-System-Desktop\packages\`)
  - `mysql-connector-java-8.0.15.jar` (Located in `Book-Borrowing-System-Desktop\packages\`)
- **Version Control**: Git

---

## Installation

### Prerequisites

1. **Java Development Kit (JDK)**: Ensure JDK 8 or higher is installed.
2. **SQLite**: No additional setup is required as SQLite is embedded.
3. **NetBeans IDE**: Ensure NetBeans IDE is installed.

### Steps to Run the Project

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Ziad-Abaza/Book-Borrowing-System-Desktop.git
   ```

2. **Open the Project in NetBeans**:
   - Launch NetBeans IDE.
   - Go to **File > Open Project**.
   - Navigate to the cloned repository folder (`Book-Borrowing-System-Desktop`) and select it.
   - Click **Open**.

3. **Add Required Libraries**:
   - Right-click on the project name in the **Projects** tab.
   - Select **Properties**.
   - In the left menu, click on **Libraries**.
   - Under the **Compile** tab, click **Add JAR/Folder**.
   - Navigate to the `packages` folder inside the project directory.
   - Select both `sqlite-jdbc-3.48.0.0.jar` and `mysql-connector-java-8.0.15.jar`.
   - Click **Open** to add the libraries.
   - Click **OK** to save the changes.

4. **Run the Application**:
   - Locate the `LoginUI` class in the `library.management.system.ui` package.
   - Right-click on the `LoginUI` class and select **Run File**.
   - The application will start, and the login window will appear.

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
![Login Page](https://github.com/Ziad-Abaza/Book-Borrowing-System-Desktop/blob/main/screenshots/login.png)

### Book Management Page
![Book Management](https://github.com/Ziad-Abaza/Book-Borrowing-System-Desktop/blob/main/screenshots/book.png)

### Student Management Page
![Student Management](https://github.com/Ziad-Abaza/Book-Borrowing-System-Desktop/blob/main/screenshots/student.png)

### User Management Page
![Issue Book](https://github.com/Ziad-Abaza/Book-Borrowing-System-Desktop/blob/main/screenshots/user.png)

---

## Database Schema

The database consists of the following tables:

1. **`book` Table**:
   - Stores book details such as name, publisher, price, status, issue date, due date, and student ID (if issued).

2. **`student` Table**:
   - Stores student details such as name, course, branch, and semester.

3. **`login` Table**:
   - Stores user credentials (user ID and password).

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
│   │   │   │   └── Main.java
│   │   │   └── ...
│   │   └── ...
│   └── ...
├── packages/
│   └── sqlite-jdbc-3.48.0.0.jar        # SQLite JDBC library
│   └── mysql-connector-java-8.0.15.jar # MySQL JDBC library
├── screenshots/                        # Screenshots of the application
├── README.md                           # Project documentation
└── database.sql                        # SQL script for database setup
```

---

## Contributors

- **Ziad Abaza** - Project Developer
- **Omar AlGamel** - Contributor ([GitHub](https://github.com/algamelomer))
- **Youssaf Mohamed** - Contributor ([GitHub](https://github.com/Youssaf-Mohamed))
- **College Name** - Borg El Arab Technological University

---

## License

This project is licensed under the **MIT License**. Feel free to use, modify, and distribute it as per the license terms.

---

## Contact

For any queries or feedback, please contact:
- **Email**: ziad.abaza@gmail.com
- **GitHub**: [Ziad-Abaza](https://github.com/Ziad-Abaza)

---

Thank you for checking out the **Book Borrowing System**! We hope this project serves as a useful resource for your college studies and beyond. 🚀
