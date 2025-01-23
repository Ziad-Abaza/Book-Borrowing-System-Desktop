package library.management.system.model;

public class Student {
    private int id;
    private String name;
    private String course;
    private String branch;
    private String semester;

    // Constructor without parameters
    public Student() {
    }

    // Constructor with parameters
    public Student(int id, String name, String course, String branch, String semester) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.branch = branch;
        this.semester = semester;
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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    // toString method to display student information
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", course='" + course + '\'' +
                ", branch='" + branch + '\'' +
                ", semester='" + semester + '\'' +
                '}';
    }
}