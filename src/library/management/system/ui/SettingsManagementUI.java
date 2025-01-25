package library.management.system.ui;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        setLocationRelativeTo(null);

        // إنشاء اللوحة الرئيسية
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));

        // إضافة HeaderPanel
        HeaderPanel headerPanel = new HeaderPanel("إدارة الإعدادات");
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // إضافة تبويبات (Tabs) للإعدادات
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 16));
        tabbedPane.addTab("تغيير كلمة المرور", createPasswordPanel());
        tabbedPane.addTab("إعدادات الإعارة", createLoanSettingsPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    // لوحة تغيير كلمة المرور
    private JPanel createPasswordPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

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
        savePasswordButton.setBackground(new Color(70, 130, 180));
        savePasswordButton.setForeground(Color.WHITE);
        savePasswordButton.setFocusPainted(false);
        savePasswordButton.addActionListener(e -> changePassword());
        panel.add(savePasswordButton, gbc);

        return panel;
    }

    // لوحة إعدادات الإعارة
    private JPanel createLoanSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // حقول إدخال إعدادات الإعارة
        JTextField defaultLoanPeriodField = new JTextField(20);
        JTextField dailyFineField = new JTextField(20);
        JTextField maxBooksField = new JTextField(20);

        // تحميل الإعدادات الحالية
        loadSettings(defaultLoanPeriodField, dailyFineField, maxBooksField);

        // إضافة الحقول إلى اللوحة
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("فترة الإعارة الافتراضية (أيام):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(defaultLoanPeriodField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("الغرامات اليومية (جنيه):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(dailyFineField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("الحد الأقصى للكتب المعارة:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(maxBooksField, gbc);

        // زر حفظ التغييرات
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveSettingsButton = new JButton("حفظ التغييرات");
        saveSettingsButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveSettingsButton.setBackground(new Color(70, 130, 180));
        saveSettingsButton.setForeground(Color.WHITE);
        saveSettingsButton.setFocusPainted(false);
        saveSettingsButton.addActionListener(e -> {
            try {
                int defaultLoanPeriod = Integer.parseInt(defaultLoanPeriodField.getText());
                double dailyFine = Double.parseDouble(dailyFineField.getText());
                int maxBooks = Integer.parseInt(maxBooksField.getText());

                saveSettings(defaultLoanPeriod, dailyFine, maxBooks);
                JOptionPane.showMessageDialog(this, "تم تحديث إعدادات الإعارة بنجاح!", "نجاح", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "يرجى إدخال قيم صحيحة!", "خطأ", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(saveSettingsButton, gbc);

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
                JOptionPane.showMessageDialog(this, "تم تغيير كلمة السر بنجاح!", "نجاح", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "كلمة المرور القديمة غير صحيحة!", "خطأ", JOptionPane.ERROR_MESSAGE);
                return;
            }

            new LoginUI().setVisible(true);
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في تغيير كلمة السر: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }

    // دالة لحفظ إعدادات الإعارة
    private void saveSettings(int defaultLoanPeriod, double dailyFine, int maxBooks) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT OR REPLACE INTO settings (key, value) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, "default_loan_period");
                stmt.setInt(2, defaultLoanPeriod);
                stmt.executeUpdate();

                stmt.setString(1, "daily_fine");
                stmt.setDouble(2, dailyFine);
                stmt.executeUpdate();

                stmt.setString(1, "max_books");
                stmt.setInt(2, maxBooks);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في حفظ الإعدادات: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }

    // دالة لتحميل إعدادات الإعارة
    private void loadSettings(JTextField defaultLoanPeriodField, JTextField dailyFineField, JTextField maxBooksField) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT value FROM settings WHERE key = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, "default_loan_period");
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        defaultLoanPeriodField.setText(rs.getString("value"));
                    }
                }

                stmt.setString(1, "daily_fine");
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        dailyFineField.setText(rs.getString("value"));
                    }
                }

                stmt.setString(1, "max_books");
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        maxBooksField.setText(rs.getString("value"));
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في تحميل الإعدادات: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Main method لتشغيل الواجهة
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SettingsManagementUI settingsUI = new SettingsManagementUI();
            settingsUI.setVisible(true);
        });
    }
}