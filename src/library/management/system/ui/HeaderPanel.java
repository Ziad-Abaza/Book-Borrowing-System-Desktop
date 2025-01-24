package library.management.system.ui;

import java.awt.*;
import javax.swing.*;

public class HeaderPanel extends JPanel {
    private JButton homeButton;
    private JLabel titleLabel;

    public HeaderPanel(String pageTitle) {
        // إعداد اللوحة
        setLayout(new BorderLayout());
        setBackground(new Color(70, 130, 180));

        // زر Home
        homeButton = new JButton();
	homeButton.setIcon(new ImageIcon("src/library/management/system/ui/Assets/menu-button.png")); 
        homeButton.setBackground(new Color(70, 130, 180));
        homeButton.setForeground(Color.WHITE);
        homeButton.setFocusPainted(false);
        homeButton.addActionListener(e -> showHomePage());
        add(homeButton, BorderLayout.WEST);

        // عنوان الصفحة
        titleLabel = new JLabel(pageTitle);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.CENTER);
    }

    // عرض صفحة الخدمات المتاحة
    private void showHomePage() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        JDialog homeDialog = new JDialog(topFrame, "الخدمات المتاحة", true);
        homeDialog.setSize(400, 300);
        homeDialog.setLocationRelativeTo(topFrame);

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
            topFrame.dispose();
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
            topFrame.dispose();
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
            topFrame.dispose();
        });
        mainPanel.add(studentManagementButton);

        // زر إدارة الإعدادات
        JButton SettingsManagementButton = new JButton("إدارة الاعدادات");
        SettingsManagementButton.setFont(new Font("Arial", Font.BOLD, 16));
        SettingsManagementButton.setBackground(new Color(70, 130, 180));
        SettingsManagementButton.setForeground(Color.WHITE);
        SettingsManagementButton.setFocusPainted(false);
        SettingsManagementButton.addActionListener(e -> {
            homeDialog.dispose();
            new SettingsManagementUI().setVisible(true);
            topFrame.dispose();
        });
        mainPanel.add(SettingsManagementButton);

        homeDialog.add(mainPanel);
        homeDialog.setVisible(true);
    }
}