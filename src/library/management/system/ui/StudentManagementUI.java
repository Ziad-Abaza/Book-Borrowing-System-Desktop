package library.management.system.ui;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import library.management.system.Session;
import library.management.system.dao.DatabaseConnection;
import library.management.system.dao.StudentDAO;
import library.management.system.model.Student;

public class StudentManagementUI extends JFrame {
    private JTable studentTable; // جدول لعرض الطلاب
    private JButton addStudentButton, deleteStudentButton, updateStudentButton, searchStudentButton; // أزرار الإدارة
    private JTextField searchField; // حقل البحث

    // Constructor لتهيئة الواجهة
    public StudentManagementUI() {
        if (!Session.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "يجب تسجيل الدخول أولاً!", "خطأ", JOptionPane.ERROR_MESSAGE);
            new LoginUI().setVisible(true);
            dispose();
        }
        // إعداد الإطار الرئيسي
        setTitle("إدارة الطلاب - نظام إدارة المكتبة");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // لجعل النافذة في وسط الشاشة

        // إنشاء اللوحة الرئيسية
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245)); // لون خلفية اللوحة

        // إضافة HeaderPanel (لوحة العنوان)
        HeaderPanel headerPanel = new HeaderPanel("إدارة الطلاب");
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // لوحة الجدول
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // هامش حول الجدول

        // إنشاء جدول الطلاب
        studentTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(studentTable); // إضافة شريط تمرير للجدول
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // لوحة الأزرار
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // تخطيط الأزرار

        // زر إضافة طالب
        addStudentButton = new JButton("إضافة طالب");
        customizeButton(addStudentButton, new Color(50, 205, 50)); // تخصيص الزر
        addStudentButton.addActionListener(e -> handleAddStudent()); // إضافة حدث النقر
        buttonPanel.add(addStudentButton);

        // زر حذف طالب
        deleteStudentButton = new JButton("حذف طالب");
        customizeButton(deleteStudentButton, new Color(220, 20, 60)); // تخصيص الزر
        deleteStudentButton.addActionListener(e -> handleDeleteStudent()); // إضافة حدث النقر
        buttonPanel.add(deleteStudentButton);

        // زر تعديل طالب
        updateStudentButton = new JButton("تعديل طالب");
        customizeButton(updateStudentButton, new Color(70, 130, 180)); // تخصيص الزر
        updateStudentButton.addActionListener(e -> handleUpdateStudent()); // إضافة حدث النقر
        buttonPanel.add(updateStudentButton);

        // زر بحث عن طالب
        searchStudentButton = new JButton("بحث عن طالب");
        customizeButton(searchStudentButton, new Color(255, 140, 0)); // تخصيص الزر
        searchStudentButton.addActionListener(e -> handleSearchStudent()); // إضافة حدث النقر
        buttonPanel.add(searchStudentButton);

        // حقل البحث
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        buttonPanel.add(searchField);

        // إضافة اللوحات إلى الإطار الرئيسي
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // إضافة اللوحة الرئيسية إلى الإطار
        add(mainPanel);

        // تحديث الجدول عند فتح الواجهة
        refreshStudentTable();
    }

    // دالة لتحديث جدول الطلاب
    private void refreshStudentTable() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            StudentDAO studentDAO = new StudentDAO(connection);
            List<Student> students = studentDAO.getAllStudents(); // جلب جميع الطلاب من قاعدة البيانات

            // تحويل قائمة الطلاب إلى نموذج جدول
            String[] columnNames = {"ID", "الاسم", "التخصص", "القسم", "الفصل"};
            Object[][] data = new Object[students.size()][5];
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                data[i][0] = student.getId();
                data[i][1] = student.getName();
                data[i][2] = student.getCourse();
                data[i][3] = student.getBranch();
                data[i][4] = student.getSemester();
            }

            studentTable.setModel(new DefaultTableModel(data, columnNames)); // تعيين النموذج للجدول
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في جلب البيانات: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }

    // دالة لإضافة طالب جديد
    private void handleAddStudent() {
        JTextField nameField = new JTextField();
        JTextField courseField = new JTextField();
        JTextField branchField = new JTextField();
        JTextField semesterField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("اسم الطالب:"));
        panel.add(nameField);
        panel.add(new JLabel("التخصص:"));
        panel.add(courseField);
        panel.add(new JLabel("القسم:"));
        panel.add(branchField);
        panel.add(new JLabel("الفصل:"));
        panel.add(semesterField);

        int result = JOptionPane.showConfirmDialog(this, panel, "إضافة طالب", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                StudentDAO studentDAO = new StudentDAO(connection);
                Student student = new Student();
                student.setName(nameField.getText());
                student.setCourse(courseField.getText());
                student.setBranch(branchField.getText());
                student.setSemester(semesterField.getText());

                studentDAO.addStudent(student); // إضافة الطالب
                refreshStudentTable(); // تحديث الجدول
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "فشل في إضافة الطالب: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // دالة لحذف طالب
    private void handleDeleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "يرجى اختيار طالب للحذف!", "خطأ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int studentId = (int) studentTable.getValueAt(selectedRow, 0);
        try (Connection connection = DatabaseConnection.getConnection()) {
            StudentDAO studentDAO = new StudentDAO(connection);
            studentDAO.deleteStudent(studentId); // حذف الطالب
            refreshStudentTable(); // تحديث الجدول
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في حذف الطالب: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }

    // دالة لتعديل طالب
    private void handleUpdateStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "يرجى اختيار طالب للتعديل!", "خطأ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int studentId = (int) studentTable.getValueAt(selectedRow, 0);
        try (Connection connection = DatabaseConnection.getConnection()) {
            StudentDAO studentDAO = new StudentDAO(connection);
            Student student = studentDAO.getStudentById(studentId);

            JTextField nameField = new JTextField(student.getName());
            JTextField courseField = new JTextField(student.getCourse());
            JTextField branchField = new JTextField(student.getBranch());
            JTextField semesterField = new JTextField(student.getSemester());

            JPanel panel = new JPanel(new GridLayout(4, 2));
            panel.add(new JLabel("اسم الطالب:"));
            panel.add(nameField);
            panel.add(new JLabel("التخصص:"));
            panel.add(courseField);
            panel.add(new JLabel("القسم:"));
            panel.add(branchField);
            panel.add(new JLabel("الفصل:"));
            panel.add(semesterField);

            int result = JOptionPane.showConfirmDialog(this, panel, "تعديل طالب", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                student.setName(nameField.getText());
                student.setCourse(courseField.getText());
                student.setBranch(branchField.getText());
                student.setSemester(semesterField.getText());
                studentDAO.updateStudent(student); // تحديث الطالب
                refreshStudentTable(); // تحديث الجدول
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في تعديل الطالب: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }

    // دالة للبحث عن طالب
    private void handleSearchStudent() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "يرجى إدخال نص للبحث!", "خطأ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            StudentDAO studentDAO = new StudentDAO(connection);
            List<Student> students = studentDAO.searchStudentsByName(searchText);

            DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
            model.setRowCount(0); // مسح الجدول الحالي

            for (Student student : students) {
                model.addRow(new Object[]{
                        student.getId(),
                        student.getName(),
                        student.getCourse(),
                        student.getBranch(),
                        student.getSemester()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في البحث: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }

    // دالة لتخصيص الأزرار
    private void customizeButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    // Main method لتشغيل الواجهة
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentManagementUI().setVisible(true));
    }
}