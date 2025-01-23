package library.management.system.ui;

import library.management.system.dao.DatabaseConnection;
import library.management.system.dao.UserDAO;
import library.management.system.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserManagementUI extends JFrame {
    private JTable userTable;
    private JButton addUserButton, deleteUserButton, updatePasswordButton, homeButton;

    public UserManagementUI() {
        // إعداد الإطار الرئيسي
        setTitle("إدارة المستخدمين - نظام إدارة المكتبة");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // لجعل النافذة في وسط الشاشة

        // إنشاء لوحة رئيسية
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // لوحة العنوان
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(70, 130, 180));

        // زر Home
        homeButton = new JButton();
        homeButton.setIcon(new ImageIcon("path/to/home_icon.png")); // استبدل بمسار أيقونة المنزل
        homeButton.setBackground(new Color(70, 130, 180));
        homeButton.setForeground(Color.WHITE);
        homeButton.setFocusPainted(false);
        homeButton.addActionListener(e -> showHomePage());
        titlePanel.add(homeButton, BorderLayout.WEST);

        // عنوان الواجهة
        JLabel titleLabel = new JLabel("إدارة المستخدمين");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // لوحة الجدول
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // إنشاء جدول المستخدمين
        userTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(userTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // لوحة الأزرار
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // زر إضافة مستخدم
        addUserButton = new JButton("إضافة مستخدم");
        addUserButton.setFont(new Font("Arial", Font.BOLD, 16));
        addUserButton.setBackground(new Color(50, 205, 50));
        addUserButton.setForeground(Color.WHITE);
        addUserButton.setFocusPainted(false);
        addUserButton.addActionListener(e -> handleAddUser());
        buttonPanel.add(addUserButton);

        // زر حذف مستخدم
        deleteUserButton = new JButton("حذف مستخدم");
        deleteUserButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteUserButton.setBackground(new Color(220, 20, 60));
        deleteUserButton.setForeground(Color.WHITE);
        deleteUserButton.setFocusPainted(false);
        deleteUserButton.addActionListener(e -> handleDeleteUser());
        buttonPanel.add(deleteUserButton);

        // زر تحديث كلمة المرور
        updatePasswordButton = new JButton("تحديث كلمة المرور");
        updatePasswordButton.setFont(new Font("Arial", Font.BOLD, 16));
        updatePasswordButton.setBackground(new Color(70, 130, 180));
        updatePasswordButton.setForeground(Color.WHITE);
        updatePasswordButton.setFocusPainted(false);
        updatePasswordButton.addActionListener(e -> handleUpdatePassword());
        buttonPanel.add(updatePasswordButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // إضافة اللوحة الرئيسية إلى الإطار
        add(mainPanel);

        // تحديث الجدول عند فتح الواجهة
        refreshUserTable();
    }

    // عرض صفحة الخدمات المتاحة
    private void showHomePage() {
        JDialog homeDialog = new JDialog(this, "الخدمات المتاحة", true);
        homeDialog.setSize(400, 300);
        homeDialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // زر إدارة الكتب
        JButton bookManagementButton = new JButton("إدارة الكتب");
        bookManagementButton.setFont(new Font("Arial", Font.BOLD, 16));
        bookManagementButton.setBackground(new Color(70, 130, 180));
        bookManagementButton.setForeground(Color.WHITE);
        bookManagementButton.setFocusPainted(false);
        bookManagementButton.addActionListener(e -> {
            homeDialog.dispose();
            new BookManagementUI().setVisible(true);
        });
        mainPanel.add(bookManagementButton);

        // زر إدارة المستخدمين
        JButton userManagementButton = new JButton("إدارة المستخدمين");
        userManagementButton.setFont(new Font("Arial", Font.BOLD, 16));
        userManagementButton.setBackground(new Color(50, 205, 50));
        userManagementButton.setForeground(Color.WHITE);
        userManagementButton.setFocusPainted(false);
        userManagementButton.addActionListener(e -> {
            homeDialog.dispose();
            new UserManagementUI().setVisible(true);
        });
        mainPanel.add(userManagementButton);

        // زر إدارة الطلاب
        JButton studentManagementButton = new JButton("إدارة الطلاب");
        studentManagementButton.setFont(new Font("Arial", Font.BOLD, 16));
        studentManagementButton.setBackground(new Color(255, 140, 0));
        studentManagementButton.setForeground(Color.WHITE);
        studentManagementButton.setFocusPainted(false);
        studentManagementButton.addActionListener(e -> {
            homeDialog.dispose();
            new StudentManagementUI().setVisible(true);
        });
        mainPanel.add(studentManagementButton);

        homeDialog.add(mainPanel);
        homeDialog.setVisible(true);
    }

    // تحديث جدول المستخدمين
    private void refreshUserTable() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);
            List<User> users = userDAO.getAllUsers();

            // تحويل قائمة المستخدمين إلى نموذج جدول
            String[] columnNames = {"User ID", "Password"};
            Object[][] data = new Object[users.size()][2];
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                data[i][0] = user.getUserId();
                data[i][1] = user.getPassword();
            }

            userTable.setModel(new DefaultTableModel(data, columnNames));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في جلب البيانات: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }

    // إضافة مستخدم
    private void handleAddUser() {
        JTextField userIdField = new JTextField();
        JTextField passwordField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("User ID:"));
        panel.add(userIdField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "إضافة مستخدم", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                UserDAO userDAO = new UserDAO(connection);
                userDAO.addUser(userIdField.getText(), passwordField.getText());
                refreshUserTable();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "فشل في إضافة المستخدم: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // حذف مستخدم
    private void handleDeleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "يرجى اختيار مستخدم للحذف!", "خطأ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String userId = (String) userTable.getValueAt(selectedRow, 0);
        try (Connection connection = DatabaseConnection.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);
            userDAO.deleteUser(userId);
            refreshUserTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في حذف المستخدم: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }

    // تحديث كلمة المرور
    private void handleUpdatePassword() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "يرجى اختيار مستخدم لتحديث كلمة المرور!", "خطأ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String userId = (String) userTable.getValueAt(selectedRow, 0);
        JTextField newPasswordField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("كلمة المرور الجديدة:"));
        panel.add(newPasswordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "تحديث كلمة المرور", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                UserDAO userDAO = new UserDAO(connection);
                userDAO.updateUserPassword(userId, newPasswordField.getText());
                refreshUserTable();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "فشل في تحديث كلمة المرور: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserManagementUI().setVisible(true));
    }
}