package library.management.system.model;

public class User {
    private String userId;
    private String password;

    // Constructor without parameters
    public User() {
    }

    // Constructor with parameters
    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // toString method to display user information
    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}