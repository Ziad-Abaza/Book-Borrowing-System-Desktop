package library.management.system.dao;
import java.sql.*;


public class DbGenerator {
        public static void createTables() {
        String sqlBook = "CREATE TABLE IF NOT EXISTS book (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " name TEXT NOT NULL,\n"
                + " publisher TEXT NOT NULL,\n"
                + " price REAL NOT NULL,\n"
                + " status TEXT DEFAULT 'available',\n"
                + " issuedate TEXT DEFAULT NULL,\n"
                + " duedate TEXT DEFAULT NULL,\n"
                + " studentid INTEGER DEFAULT NULL,\n"
                + " FOREIGN KEY (studentid) REFERENCES student (id)\n"
                + ");";

        String sqlLogin = "CREATE TABLE IF NOT EXISTS login (\n"
                + " userid TEXT PRIMARY KEY,\n"
                + " password TEXT NOT NULL\n"
                + ");";

        String sqlStudent = "CREATE TABLE IF NOT EXISTS student (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " name TEXT NOT NULL,\n"
                + " course TEXT NOT NULL,\n"
                + " branch TEXT NOT NULL,\n"
                + " semester TEXT NOT NULL\n"
                + ");";

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
