package library.management.system.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        HeaderPanel headerPanel = new HeaderPanel("إدارة الاعدادات");
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        // إضافة تبويبات (Tabs) للإعدادات
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("تغيير كلمة المرور", createPasswordPanel());
        tabbedPane.addTab("تحديث معلومات النظام", createSystemInfoPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    // لوحة تغيير كلمة المرور
    private JPanel createPasswordPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        // حقول إدخال كلمة المرور
        panel.add(new JLabel("كلمة المرور القديمة:"));
        oldPasswordField = new JPasswordField();
        panel.add(oldPasswordField);

        panel.add(new JLabel("كلمة المرور الجديدة:"));
        newPasswordField = new JPasswordField();
        panel.add(newPasswordField);

        panel.add(new JLabel("تأكيد كلمة المرور الجديدة:"));
        confirmPasswordField = new JPasswordField();
        panel.add(confirmPasswordField);

        // زر حفظ التغييرات
        savePasswordButton = new JButton("حفظ التغييرات");
        savePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePassword();
            }
        });
        panel.add(new JLabel());
        panel.add(savePasswordButton);

        return panel;
    }

    // لوحة تحديث معلومات النظام
    private JPanel createSystemInfoPanel() {
        return new JPanel();
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
        //
    }

    // Main method لتشغيل الواجهة
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SettingsManagementUI settingsUI = new SettingsManagementUI();
            settingsUI.setVisible(true);
        });
    }
}