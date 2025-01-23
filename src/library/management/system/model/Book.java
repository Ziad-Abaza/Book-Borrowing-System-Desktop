package library.management.system.model;

import java.sql.Date;

public class Book {
    private int id;
    private String name;
    private String publisher;
    private double price;
    private String status; // يمكن أن تكون القيم: "available" أو "issued"
    private Date issueDate;
    private Date dueDate;
    private int studentId; // ID الطالب الذي استعار الكتاب (إذا كان مستعارًا)

    // Constructor without parameters
    public Book() {
    }

    // Constructor with parameters
    public Book(int id, String name, String publisher, double price, String status, Date issueDate, Date dueDate, int studentId) {
        this.id = id;
        this.name = name;
        this.publisher = publisher;
        this.price = price;
        this.status = status;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.studentId = studentId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    // Check if the book is available
    public boolean isAvailable() {
        return "available".equalsIgnoreCase(this.status);
    }

    // Issue the book to a student
    public void issueBook(int studentId, Date issueDate, Date dueDate) {
        if (isAvailable()) {
            this.studentId = studentId;
            this.issueDate = issueDate;
            this.dueDate = dueDate;
            this.status = "issued";
        } else {
            throw new IllegalStateException("Book is not available for issuing.");
        }
    }

    // Return the book
    public void returnBook() {
        this.studentId = 0;
        this.issueDate = null;
        this.dueDate = null;
        this.status = "available";
    }
    
    // toString method to display book information
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", publisher='" + publisher + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                ", issueDate=" + issueDate +
                ", dueDate=" + dueDate +
                ", studentId=" + studentId +
                '}';
    }
}