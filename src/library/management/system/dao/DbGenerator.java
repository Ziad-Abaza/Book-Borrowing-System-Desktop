package library.management.system.dao;

import java.sql.*;

public class DbGenerator {
    public static void createTables() {
        String sqlBook = 
            "CREATE TABLE IF NOT EXISTS book ("
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
            "CREATE TABLE IF NOT EXISTS login ("
            + "userid TEXT PRIMARY KEY, "
            + "password TEXT NOT NULL);";

        String sqlStudent = 
            "CREATE TABLE IF NOT EXISTS student ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "name TEXT NOT NULL, "
            + "course TEXT NOT NULL, "
            + "branch TEXT NOT NULL, "
            + "semester TEXT NOT NULL);";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
            stmt.execute(sqlBook);
            stmt.execute(sqlLogin);
            stmt.execute(sqlStudent);
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        createTables();
    }
}