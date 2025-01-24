package library.management.system.ui;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import library.management.system.Session;
import library.management.system.dao.DatabaseConnection;
import library.management.system.dao.UserDAO;
import library.management.system.model.User;

public class UserManagementUI extends JFrame {
    private JTable userTable; // جدول لعرض المستخدمين
    private JButton addUserButton, deleteUserButton, updatePasswordButton; // أزرار الإدارة

    // Constructor لتهيئة الواجهة
    public UserManagementUI() {
        if (!Session.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "يجب تسجيل الدخول أولاً!", "خطأ", JOptionPane.ERROR_MESSAGE);
            new LoginUI().setVisible(true);
            dispose();
        }
        // إعداد الإطار الرئيسي
        setTitle("إدارة المستخدمين - نظام إدارة المكتبة");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // لجعل النافذة في وسط الشاشة

        // إنشاء اللوحة الرئيسية
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245)); // لون خلفية اللوحة

        // إضافة HeaderPanel (لوحة العنوان)
        HeaderPanel headerPanel = new HeaderPanel("إدارة المستخدمين");
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // لوحة الجدول
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // هامش حول الجدول

        // إنشاء جدول المستخدمين
        userTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(userTable); // إضافة شريط تمرير للجدول
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // لوحة الأزرار
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // تخطيط الأزرار

        // زر إضافة مستخدم
        addUserButton = new JButton("إضافة مستخدم");
        customizeButton(addUserButton, new Color(50, 205, 50)); // تخصيص الزر
        addUserButton.addActionListener(e -> handleAddUser()); // إضافة حدث النقر
        buttonPanel.add(addUserButton);

        // زر حذف مستخدم
        deleteUserButton = new JButton("حذف مستخدم");
        customizeButton(deleteUserButton, new Color(220, 20, 60)); // تخصيص الزر
        deleteUserButton.addActionListener(e -> handleDeleteUser()); // إضافة حدث النقر
        buttonPanel.add(deleteUserButton);

        // زر تحديث كلمة المرور
        updatePasswordButton = new JButton("تحديث كلمة المرور");
        customizeButton(updatePasswordButton, new Color(70, 130, 180)); // تخصيص الزر
        updatePasswordButton.addActionListener(e -> handleUpdatePassword()); // إضافة حدث النقر
        buttonPanel.add(updatePasswordButton);

        // إضافة اللوحات إلى الإطار الرئيسي
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // إضافة اللوحة الرئيسية إلى الإطار
        add(mainPanel);

        // تحديث الجدول عند فتح الواجهة
        refreshUserTable();
    }

    // دالة لتحديث جدول المستخدمين
    private void refreshUserTable() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);
            List<User> users = userDAO.getAllUsers(); // جلب جميع المستخدمين من قاعدة البيانات

            // تحويل قائمة المستخدمين إلى نموذج جدول
            String[] columnNames = {"User ID", "Password"};
            Object[][] data = new Object[users.size()][2];
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                data[i][0] = user.getUserId();
                data[i][1] = user.getPassword();
            }

            userTable.setModel(new DefaultTableModel(data, columnNames)); // تعيين النموذج للجدول
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في جلب البيانات: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }

    // دالة لإضافة مستخدم جديد
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
                userDAO.addUser(userIdField.getText(), passwordField.getText()); // إضافة المستخدم
                refreshUserTable(); // تحديث الجدول
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "فشل في إضافة المستخدم: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // دالة لحذف مستخدم
    private void handleDeleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "يرجى اختيار مستخدم للحذف!", "خطأ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String userId = (String) userTable.getValueAt(selectedRow, 0);
        try (Connection connection = DatabaseConnection.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);
            userDAO.deleteUser(userId); // حذف المستخدم
            refreshUserTable(); // تحديث الجدول
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في حذف المستخدم: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }

    // دالة لتحديث كلمة مرور المستخدم
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
                userDAO.updateUserPassword(userId, newPasswordField.getText()); // تحديث كلمة المرور
                refreshUserTable(); // تحديث الجدول
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "فشل في تحديث كلمة المرور: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
            }
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
        SwingUtilities.invokeLater(() -> new UserManagementUI().setVisible(true));
    }
}