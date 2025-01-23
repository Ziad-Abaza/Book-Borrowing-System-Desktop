package library.management.system.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import library.management.system.model.User;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    // Validate user credentials
    public boolean validateUser(String userId, String password) throws SQLException {
        String query = "SELECT * FROM login WHERE userid = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userId);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Add a new user
    public void addUser(String userId, String password) throws SQLException {
        String query = "INSERT INTO login (userid, password) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userId);
            stmt.setString(2, password);
            stmt.executeUpdate();
        }
    }

    // Delete a user
    public void deleteUser(String userId) throws SQLException {
        String query = "DELETE FROM login WHERE userid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        }
    }

    // Update user password
    public void updateUserPassword(String userId, String newPassword) throws SQLException {
        String query = "UPDATE login SET password = ? WHERE userid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, userId);
            stmt.executeUpdate();
        }
    }

    // Get all users
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM login";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getString("userid"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
        }
        return users;
    }
}