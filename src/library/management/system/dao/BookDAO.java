package library.management.system.dao;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import library.management.system.model.Book;
import library.management.system.model.Student;

public class BookDAO {
    private Connection connection;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // تنسيق التاريخ

    public BookDAO(Connection connection) {
        this.connection = connection;
    }

    // Add a new book
    public void addBook(Book book) throws SQLException {
        String query = "INSERT INTO book (name, publisher, price, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, book.getName());
            stmt.setString(2, book.getPublisher());
            stmt.setDouble(3, book.getPrice());
            stmt.setString(4, book.getStatus());
            stmt.executeUpdate();
        }
    }

    // Get all books
    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM book";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setName(rs.getString("name"));
                book.setPublisher(rs.getString("publisher"));
                book.setPrice(rs.getDouble("price"));
                book.setStatus(rs.getString("status"));
                book.setIssueDate(rs.getString("issuedate")); // استخدام النص بدلاً من التاريخ
                book.setDueDate(rs.getString("duedate"));     // استخدام النص بدلاً من التاريخ
                book.setStudentId(rs.getInt("studentid"));
                books.add(book);
            }
        }
        return books;
    }

    // Update book information
    public void updateBook(Book book) throws SQLException {
        String query = "UPDATE book SET name = ?, publisher = ?, price = ?, status = ?, issuedate = ?, duedate = ?, studentid = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, book.getName());
            stmt.setString(2, book.getPublisher());
            stmt.setDouble(3, book.getPrice());
            stmt.setString(4, book.getStatus());
            stmt.setString(5, book.getIssueDate()); // استخدام النص بدلاً من التاريخ
            stmt.setString(6, book.getDueDate());   // استخدام النص بدلاً من التاريخ
            stmt.setInt(7, book.getStudentId());
            stmt.setInt(8, book.getId());
            stmt.executeUpdate();
        }
    }

    // Delete a book
    public void deleteBook(int bookId) throws SQLException {
        String query = "DELETE FROM book WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
        }
    }

    // Get book by ID
    public Book getBookById(int bookId) throws SQLException {
        String query = "SELECT * FROM book WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setName(rs.getString("name"));
                    book.setPublisher(rs.getString("publisher"));
                    book.setPrice(rs.getDouble("price"));
                    book.setStatus(rs.getString("status"));
                    book.setIssueDate(rs.getString("issuedate")); // استخدام النص بدلاً من التاريخ
                    book.setDueDate(rs.getString("duedate"));     // استخدام النص بدلاً من التاريخ
                    book.setStudentId(rs.getInt("studentid"));
                    return book;
                }
            }
        }
        return null;
    }

    // Issue a book to a student
    public void issueBook(int bookId, int studentId, String issueDate, String dueDate) throws SQLException {
        String query = "UPDATE book SET status = 'issued', issuedate = ?, duedate = ?, studentid = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, issueDate); // استخدام النص بدلاً من التاريخ
            stmt.setString(2, dueDate);   // استخدام النص بدلاً من التاريخ
            stmt.setInt(3, studentId);
            stmt.setInt(4, bookId);
            stmt.executeUpdate();
        }
    }

    // Return a book
    public void returnBook(int bookId) throws SQLException {
        String query = "UPDATE book SET status = 'available', issuedate = NULL, duedate = NULL, studentid = NULL WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
        }
    }

    // Get all books issued to a student
    public List<Student> getStudentsWithIssuedBooks() throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT s.* FROM student s JOIN book b ON s.id = b.studentid WHERE b.status = 'issued'";
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

    // Get all students with overdue books
    public List<Student> getStudentsWithOverdueBooks() throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT s.* FROM student s JOIN book b ON s.id = b.studentid WHERE b.status = 'issued' AND b.duedate < date('now')";
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
}