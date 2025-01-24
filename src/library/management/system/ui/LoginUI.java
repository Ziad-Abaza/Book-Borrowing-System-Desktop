package library.management.system.ui;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.*;
import library.management.system.dao.DatabaseConnection;
import library.management.system.dao.UserDAO;
import library.management.system.Session;

public class LoginUI extends JFrame {
    private JTextField userIdField; 
    private JPasswordField passwordField; 
    private JButton loginButton, signUpButton;

    // Constructor لتهيئة الواجهة
    public LoginUI() {
        // إعداد الإطار الرئيسي
        setTitle("تسجيل الدخول - نظام إدارة المكتبة");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // لجعل النافذة في وسط الشاشة

        // إنشاء اللوحة الرئيسية
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245)); // لون خلفية فاتح

        // لوحة العنوان
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180)); // لون أزرق
        JLabel titleLabel = new JLabel("تسجيل الدخول");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36)); // زيادة حجم الخط
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // لوحة الحقول والأزرار
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout()); // استخدام GridBagLayout للتخطيط المرن
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // زيادة الهوامش
        formPanel.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // زيادة المسافات بين العناصر

        // حقل اسم المستخدم
        JLabel userIdLabel = new JLabel("اسم المستخدم:");
        userIdLabel.setFont(new Font("Arial", Font.PLAIN, 20)); // زيادة حجم الخط
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(userIdLabel, gbc);

        userIdField = new JTextField(20); // زيادة حجم الحقل
        userIdField.setFont(new Font("Arial", Font.PLAIN, 20)); // زيادة حجم الخط
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(userIdField, gbc);

        // حقل كلمة المرور
        JLabel passwordLabel = new JLabel("كلمة المرور:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20)); // زيادة حجم الخط
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20); // زيادة حجم الحقل
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20)); // زيادة حجم الخط
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(passwordField, gbc);

        // زر تسجيل الدخول
        loginButton = new JButton("تسجيل الدخول");
        customizeButton(loginButton, new Color(70, 130, 180)); // تخصيص الزر
        loginButton.addActionListener(e -> handleLogin()); // إضافة حدث النقر
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);

        // زر التسجيل
        signUpButton = new JButton("تسجيل جديد");
        customizeButton(signUpButton, new Color(50, 205, 50)); // تخصيص الزر
        signUpButton.addActionListener(e -> handleSignUp()); // إضافة حدث النقر
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(signUpButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // إضافة اللوحة الرئيسية إلى الإطار
        add(mainPanel);
    }

    // دالة لتسجيل الدخول
    private void handleLogin() {
        String userId = userIdField.getText();
        String password = new String(passwordField.getPassword());

        if (userId.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "يرجى ملء جميع الحقول!", "خطأ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);
            if (userDAO.validateUser(userId, password)) {
                Session.login(userId); // session started
                JOptionPane.showMessageDialog(this, "تم تسجيل الدخول بنجاح!", "نجاح", JOptionPane.INFORMATION_MESSAGE);
                new BookManagementUI().setVisible(true); // افتح واجهة إدارة الكتب
                dispose(); // إغلاق نافذة تسجيل الدخول
            } else {
                JOptionPane.showMessageDialog(this, "اسم المستخدم أو كلمة المرور غير صحيحة!", "خطأ", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل الاتصال بقاعدة البيانات: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }

    // دالة للتسجيل
    private void handleSignUp() {
        String userId = userIdField.getText();
        String password = new String(passwordField.getPassword());

        if (userId.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "يرجى ملء جميع الحقول!", "خطأ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);
            userDAO.addUser(userId, password); // إضافة مستخدم جديد
            JOptionPane.showMessageDialog(this, "تم تسجيل المستخدم بنجاح!", "نجاح", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في تسجيل المستخدم: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }

    // دالة لتخصيص الأزرار
    private void customizeButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
    }

    // Main method لتشغيل الواجهة
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}