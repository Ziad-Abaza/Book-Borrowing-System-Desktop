package library.management.system.dao;

import java.sql.*;


public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:library.db";
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


}