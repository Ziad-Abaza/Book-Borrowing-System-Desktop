package library.management.system.ui;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.*;
import library.management.system.Session;
import library.management.system.dao.DatabaseConnection;
import library.management.system.dao.UserDAO;

public class SettingsManagementUI extends JFrame {
    private JPasswordField oldPasswordField, newPasswordField, confirmPasswordField;
    private JButton savePasswordButton;

    public SettingsManagementUI() {
        if (!Session.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "يجب تسجيل الدخول أولاً!", "خطأ", JOptionPane.ERROR_MESSAGE);
            new LoginUI().setVisible(true);
            dispose();
            return;
        }

        setTitle("إدارة الإعدادات - نظام إدارة المكتبة");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // لجعل النافذة في وسط الشاشة

        // إنشاء اللوحة الرئيسية
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245)); // لون خلفية متوافق مع التصميم السابق

        // إضافة HeaderPanel
        HeaderPanel headerPanel = new HeaderPanel("إدارة الإعدادات");
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // إضافة تبويبات (Tabs) للإعدادات
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 16)); // خط متوافق مع التصميم السابق
        tabbedPane.addTab("تغيير كلمة المرور", createPasswordPanel());
        tabbedPane.addTab("تحديث معلومات النظام", createSystemInfoPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    // لوحة تغيير كلمة المرور
    private JPanel createPasswordPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 245, 245)); // لون خلفية متوافق مع التصميم السابق

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // هوامش بين العناصر

        // حقول إدخال كلمة المرور
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("كلمة المرور القديمة:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        oldPasswordField = new JPasswordField(20);
        oldPasswordField.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(oldPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("كلمة المرور الجديدة:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        newPasswordField = new JPasswordField(20);
        newPasswordField.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(newPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("تأكيد كلمة المرور الجديدة:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(confirmPasswordField, gbc);

        // زر حفظ التغييرات
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        savePasswordButton = new JButton("حفظ التغييرات");
        savePasswordButton.setFont(new Font("Arial", Font.BOLD, 16));
        savePasswordButton.setBackground(new Color(70, 130, 180)); // لون الزر متوافق مع التصميم السابق
        savePasswordButton.setForeground(Color.WHITE);
        savePasswordButton.setFocusPainted(false);
        savePasswordButton.addActionListener(e -> changePassword());
        panel.add(savePasswordButton, gbc);

        return panel;
    }

    // لوحة تحديث معلومات النظام
    private JPanel createSystemInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 245, 245)); // لون خلفية متوافق مع التصميم السابق

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // هوامش بين العناصر

        // يمكن إضافة حقول إدخال لتحديث معلومات النظام هنا
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("اسم المكتبة:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField libraryNameField = new JTextField(20);
        libraryNameField.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(libraryNameField, gbc);

        // زر حفظ التغييرات
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveSystemInfoButton = new JButton("حفظ التغييرات");
        saveSystemInfoButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveSystemInfoButton.setBackground(new Color(70, 130, 180)); // لون الزر متوافق مع التصميم السابق
        saveSystemInfoButton.setForeground(Color.WHITE);
        saveSystemInfoButton.setFocusPainted(false);
        saveSystemInfoButton.addActionListener(e -> updateSystemInfo());
        panel.add(saveSystemInfoButton, gbc);

        return panel;
    }

    // دالة لتغيير كلمة المرور
    private void changePassword() {
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // التحقق من صحة المدخلات
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "يرجى ملء جميع الحقول!", "خطأ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "كلمة المرور الجديدة غير متطابقة!", "خطأ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // تغيير كلمة المرور في قاعدة البيانات
        try (Connection connection = DatabaseConnection.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);
            if (userDAO.validateUser(Session.getCurrentUserId(), oldPassword)) {
                userDAO.updateUserPassword(Session.getCurrentUserId(), newPassword);
                JOptionPane.showMessageDialog(this, "تم تغيير كلمة السر بنجاح!", "نجاح",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "كلمة المرور القديمة غير صحيحة!", "خطأ", JOptionPane.ERROR_MESSAGE);
                return;
            }

            new LoginUI().setVisible(true);
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في تغيير كلمة السر: " + e.getMessage(), "خطأ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // دالة لتحديث معلومات النظام
    private void updateSystemInfo() {
        // يمكن إضافة منطق لتحديث معلومات النظام هنا
        JOptionPane.showMessageDialog(this, "تم تحديث معلومات النظام بنجاح!", "نجاح",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Main method لتشغيل الواجهة
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SettingsManagementUI settingsUI = new SettingsManagementUI();
            settingsUI.setVisible(true);
        });
    }
}