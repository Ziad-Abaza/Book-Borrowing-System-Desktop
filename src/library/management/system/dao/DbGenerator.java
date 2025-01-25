package library.management.system.dao;

import java.sql.*;

public class DbGenerator {
    public static void createTables() {
        String dropBookTable = "DROP TABLE IF EXISTS book;";
        String dropLoginTable = "DROP TABLE IF EXISTS login;";
        String dropStudentTable = "DROP TABLE IF EXISTS student;";

        String sqlBook = 
            "CREATE TABLE book ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "name TEXT NOT NULL, "
            + "publisher TEXT NOT NULL, "
            + "price REAL NOT NULL, "
            + "status TEXT CHECK(status IN ('available', 'issued')) DEFAULT 'available', "
            + "issuedate TEXT, "  
            + "duedate TEXT, "    
            + "studentid INTEGER, "
            + "FOREIGN KEY (studentid) REFERENCES student(id) ON DELETE SET NULL);";

        String sqlLogin = 
            "CREATE TABLE login ("
            + "userid TEXT PRIMARY KEY, "
            + "password TEXT NOT NULL);";

        String sqlStudent = 
            "CREATE TABLE student ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "name TEXT NOT NULL, "
            + "course TEXT NOT NULL, "
            + "branch TEXT NOT NULL, "
            + "semester TEXT NOT NULL);";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(dropBookTable);
            stmt.execute(dropLoginTable);
            stmt.execute(dropStudentTable);
            System.out.println("Old tables dropped successfully.");

            stmt.execute("PRAGMA foreign_keys = ON;");
            stmt.execute(sqlBook);
            stmt.execute(sqlLogin);
            stmt.execute(sqlStudent);
            System.out.println("New tables created successfully.");

            insertAdminUser(conn);

            insertRealisticBooks(conn);
            insertRealisticStudents(conn);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void insertAdminUser(Connection conn) {
        String sql = "INSERT OR IGNORE INTO login (userid, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "admin");
            pstmt.setString(2, "admin");
            pstmt.executeUpdate();
            System.out.println("Admin user inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting admin user: " + e.getMessage());
        }
    }

    private static void insertRealisticBooks(Connection conn) {
        String sql = "INSERT OR IGNORE INTO book (name, publisher, price, status) VALUES (?, ?, ?, ?)";
        String[][] books = {
            {"Clean Code: A Handbook of Agile Software Craftsmanship", "Prentice Hall", "45.99", "available"},
            {"Design Patterns: Elements of Reusable Object-Oriented Software", "Addison-Wesley", "54.99", "available"},
            {"The Pragmatic Programmer: Your Journey to Mastery", "Addison-Wesley", "39.99", "available"},
            {"Introduction to Algorithms", "MIT Press", "89.99", "available"},
            {"Structure and Interpretation of Computer Programs", "MIT Press", "59.99", "available"},
            {"You Don't Know JS: Up & Going", "O'Reilly Media", "19.99", "available"},
            {"Eloquent JavaScript: A Modern Introduction to Programming", "No Starch Press", "29.99", "available"},
            {"Head First Java", "O'Reilly Media", "49.99", "available"},
            {"Python Crash Course", "No Starch Press", "34.99", "available"},
            {"Learning SQL", "O'Reilly Media", "29.99", "available"}
        };

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String[] book : books) {
                pstmt.setString(1, book[0]);
                pstmt.setString(2, book[1]);
                pstmt.setDouble(3, Double.parseDouble(book[2]));
                pstmt.setString(4, book[3]);
                pstmt.executeUpdate();
            }
            System.out.println("Realistic books inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting realistic books: " + e.getMessage());
        }
    }

    private static void insertRealisticStudents(Connection conn) {
        String sql = "INSERT OR IGNORE INTO student (name, course, branch, semester) VALUES (?, ?, ?, ?)";
        String[][] students = {
            {"Ali Ahmed", "Computer Science", "Software Engineering", "Third"},
            {"Fatima Khalid", "Information Technology", "Data Science", "Fourth"},
            {"Omar Hassan", "Computer Engineering", "Networks", "Second"},
            {"Layla Mahmoud", "Artificial Intelligence", "Machine Learning", "Fifth"},
            {"Yousef Ibrahim", "Cybersecurity", "Ethical Hacking", "First"},
            {"Aisha Salem", "Software Engineering", "Web Development", "Third"},
            {"Khalid Abdullah", "Data Science", "Big Data", "Fourth"},
            {"Noura Ali", "Computer Science", "Mobile Development", "Second"},
            {"Mohammed Saleh", "Information Systems", "Database Management", "Fifth"},
            {"Sara Ahmed", "Artificial Intelligence", "Natural Language Processing", "First"}
        };

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String[] student : students) {
                pstmt.setString(1, student[0]);
                pstmt.setString(2, student[1]);
                pstmt.setString(3, student[2]);
                pstmt.setString(4, student[3]);
                pstmt.executeUpdate();
            }
            System.out.println("Realistic students inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting realistic students: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        createTables();
    }
}