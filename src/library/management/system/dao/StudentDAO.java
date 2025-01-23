package library.management.system.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import library.management.system.model.Student;

public class StudentDAO {
    private Connection connection;

    public StudentDAO(Connection connection) {
        this.connection = connection;
    }

    // Add a new student
    public void addStudent(Student student) throws SQLException {
        String query = "INSERT INTO student (name, course, branch, semester) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getCourse());
            stmt.setString(3, student.getBranch());
            stmt.setString(4, student.getSemester());
            stmt.executeUpdate();
        }
    }

    // Get all students
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM student";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setCourse(rs.getString("course"));
                student.setBranch(rs.getString("branch"));
                student.setSemester(rs.getString("semester"));
                students.add(student);
            }
        }
        return students;
    }

    // Update student information
    public void updateStudent(Student student) throws SQLException {
        String query = "UPDATE student SET name = ?, course = ?, branch = ?, semester = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getCourse());
            stmt.setString(3, student.getBranch());
            stmt.setString(4, student.getSemester());
            stmt.setInt(5, student.getId());
            stmt.executeUpdate();
        }
    }

    // Delete a student
    public void deleteStudent(int studentId) throws SQLException {
        String query = "DELETE FROM student WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            stmt.executeUpdate();
        }
    }

    // Get student by ID
    public Student getStudentById(int studentId) throws SQLException {
        String query = "SELECT * FROM student WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setName(rs.getString("name"));
                    student.setCourse(rs.getString("course"));
                    student.setBranch(rs.getString("branch"));
                    student.setSemester(rs.getString("semester"));
                    return student;
                }
            }
        }
        return null;
    }

    // Get student by name
    public List<Student> searchStudentsByName(String name) throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM student WHERE name LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + name + "%");  
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setName(rs.getString("name"));
                    student.setCourse(rs.getString("course"));
                    student.setBranch(rs.getString("branch"));
                    student.setSemester(rs.getString("semester"));
                    students.add(student);
                }
            }
        }
        return students;
    }
}