package library.management.system.ui;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import javax.swing.*;
import library.management.system.dao.DatabaseConnection;

public class DatePickerDialog extends JDialog {
    private JComboBox<Integer> dayComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private JButton okButton;
    private JButton cancelButton;
    private boolean confirmed;

    public DatePickerDialog(JFrame parent) {
        super(parent, "تحديد تاريخ الاسترداد", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // إعداد قوائم اليوم والشهر والسنة
        Integer[] days = new Integer[31];
        for (int i = 1; i <= 31; i++) {
            days[i - 1] = i;
        }

        String[] months = new String[] {
            "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو",
            "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
        };

        Integer[] years = new Integer[10];
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 10; i++) {
            years[i] = currentYear + i;
        }

        dayComboBox = new JComboBox<>(days);
        monthComboBox = new JComboBox<>(months);
        yearComboBox = new JComboBox<>(years);

        // تعيين التاريخ الحالي كقيمة افتراضية
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH); // الأشهر تبدأ من 0
        int currentYearValue = calendar.get(Calendar.YEAR);

        dayComboBox.setSelectedItem(currentDay);
        monthComboBox.setSelectedIndex(currentMonth);
        yearComboBox.setSelectedItem(currentYearValue);

        // إضافة فترة الإعارة الافتراضية
        int defaultLoanPeriod = getDefaultLoanPeriod();
        if (defaultLoanPeriod > 0) {
            calendar.add(Calendar.DAY_OF_MONTH, defaultLoanPeriod); // إضافة فترة الإعارة إلى التاريخ الحالي
            dayComboBox.setSelectedItem(calendar.get(Calendar.DAY_OF_MONTH));
            monthComboBox.setSelectedIndex(calendar.get(Calendar.MONTH));
            yearComboBox.setSelectedItem(calendar.get(Calendar.YEAR));
        }

        mainPanel.add(new JLabel("اليوم:"));
        mainPanel.add(dayComboBox);
        mainPanel.add(new JLabel("الشهر:"));
        mainPanel.add(monthComboBox);
        mainPanel.add(new JLabel("السنة:"));
        mainPanel.add(yearComboBox);

        okButton = new JButton("موافق");
        cancelButton = new JButton("إلغاء");

        okButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getSelectedDate() {
        if (confirmed) {
            int day = (int) dayComboBox.getSelectedItem();
            int month = monthComboBox.getSelectedIndex() + 1; // الأشهر تبدأ من 0
            int year = (int) yearComboBox.getSelectedItem();

            return String.format("%04d-%02d-%02d", year, month, day);
        }
        return null;
    }

    // دالة للحصول على فترة الإعارة الافتراضية من جدول settings
    private int getDefaultLoanPeriod() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT value FROM settings WHERE key = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, "default_loan_period");
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Integer.parseInt(rs.getString("value"));
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في تحميل فترة الإعارة الافتراضية: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
        }
        return 7; // قيمة افتراضية إذا لم يتم العثور على الإعداد
    }
}