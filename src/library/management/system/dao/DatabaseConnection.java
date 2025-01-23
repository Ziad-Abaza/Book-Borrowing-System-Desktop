package library.management.system.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/javaLibrary";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            System.out.println("تم الاتصال بقاعدة البيانات بنجاح!");
        } catch (SQLException e) {
            System.err.println("فشل الاتصال بقاعدة البيانات: " + e.getMessage());
        }
    }
}