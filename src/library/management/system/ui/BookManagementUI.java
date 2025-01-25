package library.management.system.ui;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import library.management.system.Session;
import library.management.system.dao.BookDAO;
import library.management.system.dao.DatabaseConnection;
import library.management.system.dao.StudentDAO;
import library.management.system.model.Book;
import library.management.system.model.Student;

public class BookManagementUI extends JFrame {
    private JTable bookTable;
    private JButton addBookButton, deleteBookButton, updateBookButton, searchBookButton;
    private JButton issueBookButton, returnBookButton, viewIssuedBooksButton, viewStudentsWithBooksButton,
            viewOverdueBooksButton;
    private JTextField searchField;

    public BookManagementUI() {
        if (!Session.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "يجب تسجيل الدخول أولاً!", "خطأ", JOptionPane.ERROR_MESSAGE);
            new LoginUI().setVisible(true);
            dispose();
            return;
        }
        // إعداد الإطار الرئيسي
        setTitle("إدارة المستخدمين - نظام إدارة المكتبة");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // إنشاء لوحة رئيسية
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // إضافة HeaderPanel
        HeaderPanel headerPanel = new HeaderPanel("إدارة الكتب");
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // لوحة الجدول
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // إنشاء جدول الكتب
        bookTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(bookTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // لوحة الأزرار (إدارة الكتب)
        JPanel bookManagementPanel = new JPanel();
        bookManagementPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // زر إضافة كتاب
        addBookButton = new JButton("إضافة كتاب");
        addBookButton.setFont(new Font("Arial", Font.BOLD, 16));
        addBookButton.setBackground(new Color(50, 205, 50));
        addBookButton.setForeground(Color.WHITE);
        addBookButton.setFocusPainted(false);
        addBookButton.addActionListener(e -> handleAddBook());
        bookManagementPanel.add(addBookButton);

        // زر حذف كتاب
        deleteBookButton = new JButton("حذف كتاب");
        deleteBookButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteBookButton.setBackground(new Color(220, 20, 60));
        deleteBookButton.setForeground(Color.WHITE);
        deleteBookButton.setFocusPainted(false);
        deleteBookButton.addActionListener(e -> handleDeleteBook());
        bookManagementPanel.add(deleteBookButton);

        // زر تعديل كتاب
        updateBookButton = new JButton("تعديل كتاب");
        updateBookButton.setFont(new Font("Arial", Font.BOLD, 16));
        updateBookButton.setBackground(new Color(70, 130, 180));
        updateBookButton.setForeground(Color.WHITE);
        updateBookButton.setFocusPainted(false);
        updateBookButton.addActionListener(e -> handleUpdateBook());
        bookManagementPanel.add(updateBookButton);

        // زر بحث عن كتاب
        searchBookButton = new JButton("بحث عن كتاب");
        searchBookButton.setFont(new Font("Arial", Font.BOLD, 16));
        searchBookButton.setBackground(new Color(255, 140, 0));
        searchBookButton.setForeground(Color.WHITE);
        searchBookButton.setFocusPainted(false);
        searchBookButton.addActionListener(e -> handleSearchBook());
        bookManagementPanel.add(searchBookButton);

        // حقل البحث
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        bookManagementPanel.add(searchField);

        // لوحة الأزرار (إدارة الإعارة)
        JPanel issueManagementPanel = new JPanel();
        issueManagementPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // زر إعارة كتاب
        issueBookButton = new JButton("إعارة كتاب");
        issueBookButton.setFont(new Font("Arial", Font.BOLD, 16));
        issueBookButton.setBackground(new Color(70, 130, 180));
        issueBookButton.setForeground(Color.WHITE);
        issueBookButton.setFocusPainted(false);
        issueBookButton.addActionListener(e -> handleIssueBook());
        issueManagementPanel.add(issueBookButton);

        // زر إعادة كتاب
        returnBookButton = new JButton("إعادة كتاب");
        returnBookButton.setFont(new Font("Arial", Font.BOLD, 16));
        returnBookButton.setBackground(new Color(50, 205, 50));
        returnBookButton.setForeground(Color.WHITE);
        returnBookButton.setFocusPainted(false);
        returnBookButton.addActionListener(e -> handleReturnBook());
        issueManagementPanel.add(returnBookButton);

        // زر استعراض الكتب المعارة
        viewIssuedBooksButton = new JButton("استعراض الكتب المعارة");
        viewIssuedBooksButton.setFont(new Font("Arial", Font.BOLD, 16));
        viewIssuedBooksButton.setBackground(new Color(255, 140, 0));
        viewIssuedBooksButton.setForeground(Color.WHITE);
        viewIssuedBooksButton.setFocusPainted(false);
        viewIssuedBooksButton.addActionListener(e -> handleViewIssuedBooks());
        issueManagementPanel.add(viewIssuedBooksButton);

        // زر استعراض الطلاب الذين استعاروا كتبًا
        viewStudentsWithBooksButton = new JButton("الطلاب المستعيرين");
        viewStudentsWithBooksButton.setFont(new Font("Arial", Font.BOLD, 16));
        viewStudentsWithBooksButton.setBackground(new Color(70, 130, 180));
        viewStudentsWithBooksButton.setForeground(Color.WHITE);
        viewStudentsWithBooksButton.setFocusPainted(false);
        viewStudentsWithBooksButton.addActionListener(e -> handleViewStudentsWithBooks());
        issueManagementPanel.add(viewStudentsWithBooksButton);

        // زر استعراض الطلاب الذين تجاوزوا موعد الإعادة
        viewOverdueBooksButton = new JButton("الطلاب المتأخرين");
        viewOverdueBooksButton.setFont(new Font("Arial", Font.BOLD, 16));
        viewOverdueBooksButton.setBackground(new Color(220, 20, 60));
        viewOverdueBooksButton.setForeground(Color.WHITE);
        viewOverdueBooksButton.setFocusPainted(false);
        viewOverdueBooksButton.addActionListener(e -> handleViewOverdueBooks());
        issueManagementPanel.add(viewOverdueBooksButton);

        // إضافة اللوحات إلى الإطار الرئيسي
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));
        buttonPanel.add(bookManagementPanel);
        buttonPanel.add(issueManagementPanel);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // إضافة اللوحة الرئيسية إلى الإطار
        add(mainPanel);

        // تحديث الجدول عند فتح الواجهة
        refreshBookTable();
    }

    // تحديث جدول الكتب
    private void refreshBookTable() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            BookDAO bookDAO = new BookDAO(connection);
            List<Book> books = bookDAO.getAllBooks();

            // تحويل قائمة الكتب إلى نموذج جدول
            String[] columnNames = { "ID", "الاسم", "الناشر", "السعر", "الحالة", "تاريخ الإعارة", "تاريخ الاستحقاق",
                    "معرف الطالب" };
            Object[][] data = new Object[books.size()][8];
            for (int i = 0; i < books.size(); i++) {
                Book book = books.get(i);
                data[i][0] = book.getId();
                data[i][1] = book.getName();
                data[i][2] = book.getPublisher();
                data[i][3] = book.getPrice();
                data[i][4] = book.getStatus();
                data[i][5] = book.getIssueDate();
                data[i][6] = book.getDueDate();
                data[i][7] = book.getStudentId();
            }

            bookTable.setModel(new DefaultTableModel(data, columnNames));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في جلب البيانات: " + e.getMessage(), "خطأ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // إضافة كتاب
    private void handleAddBook() {
        JTextField nameField = new JTextField();
        JTextField publisherField = new JTextField();
        JTextField priceField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("اسم الكتاب:"));
        panel.add(nameField);
        panel.add(new JLabel("الناشر:"));
        panel.add(publisherField);
        panel.add(new JLabel("السعر:"));
        panel.add(priceField);

        int result = JOptionPane.showConfirmDialog(this, panel, "إضافة كتاب", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                BookDAO bookDAO = new BookDAO(connection);
                Book book = new Book();
                book.setName(nameField.getText());
                book.setPublisher(publisherField.getText());
                book.setPrice(Double.parseDouble(priceField.getText()));
                book.setStatus("available");

                bookDAO.addBook(book);
                refreshBookTable();
            } catch (SQLException | NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "فشل في إضافة الكتاب: " + e.getMessage(), "خطأ",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // حذف كتاب
    private void handleDeleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "يرجى اختيار كتاب للحذف!", "خطأ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bookId = (int) bookTable.getValueAt(selectedRow, 0);
        try (Connection connection = DatabaseConnection.getConnection()) {
            BookDAO bookDAO = new BookDAO(connection);
            bookDAO.deleteBook(bookId);
            refreshBookTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في حذف الكتاب: " + e.getMessage(), "خطأ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // تعديل كتاب
    private void handleUpdateBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "يرجى اختيار كتاب للتعديل!", "خطأ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bookId = (int) bookTable.getValueAt(selectedRow, 0);
        try (Connection connection = DatabaseConnection.getConnection()) {
            BookDAO bookDAO = new BookDAO(connection);
            Book book = bookDAO.getBookById(bookId);

            JTextField nameField = new JTextField(book.getName());
            JTextField publisherField = new JTextField(book.getPublisher());
            JTextField priceField = new JTextField(String.valueOf(book.getPrice()));

            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("اسم الكتاب:"));
            panel.add(nameField);
            panel.add(new JLabel("الناشر:"));
            panel.add(publisherField);
            panel.add(new JLabel("السعر:"));
            panel.add(priceField);

            int result = JOptionPane.showConfirmDialog(this, panel, "تعديل كتاب", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                book.setName(nameField.getText());
                book.setPublisher(publisherField.getText());
                book.setPrice(Double.parseDouble(priceField.getText()));
                bookDAO.updateBook(book);
                refreshBookTable();
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "فشل في تعديل الكتاب: " + e.getMessage(), "خطأ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // بحث عن كتاب
    private void handleSearchBook() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "يرجى إدخال نص للبحث!", "خطأ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            BookDAO bookDAO = new BookDAO(connection);
            List<Book> books = bookDAO.getAllBooks();

            DefaultTableModel model = (DefaultTableModel) bookTable.getModel();
            model.setRowCount(0); // مسح الجدول الحالي

            for (Book book : books) {
                if (book.getName().toLowerCase().contains(searchText.toLowerCase())) {
                    model.addRow(new Object[] {
                            book.getId(),
                            book.getName(),
                            book.getPublisher(),
                            book.getPrice(),
                            book.getStatus(),
                            book.getIssueDate(), // عرض التاريخ كنص
                            book.getDueDate(),   // عرض التاريخ كنص
                            book.getStudentId()
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في البحث: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }

    // إعارة كتاب
    private void handleIssueBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "يرجى اختيار كتاب للإعارة!", "خطأ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bookId = (int) bookTable.getValueAt(selectedRow, 0);

        // إنشاء نافذة البحث التلقائي
        JDialog searchDialog = new JDialog(this, "إعارة كتاب", true);
        searchDialog.setSize(400, 300);
        searchDialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // حقل البحث
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        mainPanel.add(searchField, BorderLayout.NORTH);

        // قائمة عرض النتائج
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> resultList = new JList<>(listModel);
        resultList.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(resultList);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // زر الإعارة
        JButton issueButton = new JButton("إعارة الكتاب");
        issueButton.setFont(new Font("Arial", Font.BOLD, 16));
        issueButton.setBackground(new Color(70, 130, 180));
        issueButton.setForeground(Color.WHITE);
        issueButton.setFocusPainted(false);
        mainPanel.add(issueButton, BorderLayout.SOUTH);

        searchDialog.add(mainPanel);

        // DocumentListener للبحث التلقائي
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchStudents();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchStudents();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchStudents();
            }

            private void searchStudents() {
                String searchText = searchField.getText().trim();
                if (searchText.isEmpty()) {
                    listModel.clear();
                    return;
                }

                try (Connection connection = DatabaseConnection.getConnection()) {
                    StudentDAO studentDAO = new StudentDAO(connection);
                    List<Student> students = studentDAO.searchStudentsByName(searchText);

                    listModel.clear();
                    for (Student student : students) {
                        listModel.addElement(student.getName() + " - " + student.getCourse());
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(searchDialog, "فشل في البحث: " + ex.getMessage(), "خطأ",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // معالجة اختيار الطالب وإعارة الكتاب
        issueButton.addActionListener(e -> {
            String selectedStudent = resultList.getSelectedValue();
            if (selectedStudent == null) {
                // إذا لم يتم اختيار طالب، يتم فتح نافذة لإدخال بيانات الطالب الجديد
                addNewStudentAndIssueBook(bookId, searchDialog, searchField.getText());
                return;
            }

            // استخراج اسم الطالب من النص المعروض
            String studentName = selectedStudent.split(" - ")[0];

            try (Connection connection = DatabaseConnection.getConnection()) {
                StudentDAO studentDAO = new StudentDAO(connection);
                BookDAO bookDAO = new BookDAO(connection);

                // البحث عن الطالب بالاسم
                List<Student> students = studentDAO.searchStudentsByName(studentName);

                if (students.isEmpty()) {
                    // إذا لم يتم العثور على الطالب، يتم فتح نافذة لإدخال بيانات الطالب الجديد
                    addNewStudentAndIssueBook(bookId, searchDialog, studentName);
                    return;
                }

                // استخدام الطالب الأول من القائمة
                Student student = students.get(0);

                // إعارة الكتاب للطالب
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String issueDate = dateFormat.format(new java.util.Date()); // التاريخ الحالي
                String dueDate = dateFormat.format(new java.util.Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)); // بعد أسبوع

                bookDAO.issueBook(bookId, student.getId(), issueDate, dueDate);
                refreshBookTable();
                JOptionPane.showMessageDialog(searchDialog, "تم إعارة الكتاب بنجاح!", "نجاح",
                        JOptionPane.INFORMATION_MESSAGE);
                searchDialog.dispose(); // إغلاق نافذة البحث
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(searchDialog, "فشل في إعارة الكتاب: " + ex.getMessage(), "خطأ",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        searchDialog.setVisible(true);
    }

 // إضافة طالب جديد وإعارة الكتاب له
private void addNewStudentAndIssueBook(int bookId, JDialog parentDialog, String studentName) {
    JTextField nameField = new JTextField(studentName);
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

    int result = JOptionPane.showConfirmDialog(parentDialog, panel, "إضافة طالب جديد",
            JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            StudentDAO studentDAO = new StudentDAO(connection);
            BookDAO bookDAO = new BookDAO(connection);

            // إنشاء طالب جديد
            Student newStudent = new Student();
            newStudent.setName(nameField.getText());
            newStudent.setCourse(courseField.getText());
            newStudent.setBranch(branchField.getText());
            newStudent.setSemester(semesterField.getText());

            // إضافة الطالب إلى قاعدة البيانات
            studentDAO.addStudent(newStudent);

            // البحث عن الطالب المضاف حديثًا
            List<Student> students = studentDAO.searchStudentsByName(newStudent.getName());

            if (students.isEmpty()) {
                throw new SQLException("فشل في العثور على الطالب المضاف حديثًا.");
            }

            // استخدام الطالب الأول من القائمة
            Student student = students.get(0);

            // إعارة الكتاب للطالب الجديد
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String issueDate = dateFormat.format(new java.util.Date()); // التاريخ الحالي
            String dueDate = dateFormat.format(new java.util.Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)); // بعد أسبوع

            bookDAO.issueBook(bookId, student.getId(), issueDate, dueDate);
            refreshBookTable();
            JOptionPane.showMessageDialog(parentDialog, "تم إضافة الطالب وإعارة الكتاب بنجاح!", "نجاح",
                    JOptionPane.INFORMATION_MESSAGE);
            parentDialog.dispose(); // إغلاق نافذة البحث
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(parentDialog, "فشل في إضافة الطالب أو إعارة الكتاب: " + ex.getMessage(),
                    "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }
}

    // إعادة كتاب
    private void handleReturnBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "يرجى اختيار كتاب للإعادة!", "خطأ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bookId = (int) bookTable.getValueAt(selectedRow, 0);
        try (Connection connection = DatabaseConnection.getConnection()) {
            BookDAO bookDAO = new BookDAO(connection);
            bookDAO.returnBook(bookId);
            refreshBookTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في إعادة الكتاب: " + e.getMessage(), "خطأ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // استعراض الكتب المعارة
    private void handleViewIssuedBooks() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            BookDAO bookDAO = new BookDAO(connection);
            List<Book> issuedBooks = bookDAO.getAllBooks().stream()
                    .filter(book -> "issued".equals(book.getStatus()))
                    .toList();

            String[] columnNames = { "ID", "الاسم", "الناشر", "السعر", "تاريخ الإعارة", "تاريخ الاستحقاق",
                    "معرف الطالب" };
            Object[][] data = new Object[issuedBooks.size()][7];
            for (int i = 0; i < issuedBooks.size(); i++) {
                Book book = issuedBooks.get(i);
                data[i][0] = book.getId();
                data[i][1] = book.getName();
                data[i][2] = book.getPublisher();
                data[i][3] = book.getPrice();
                data[i][4] = book.getIssueDate();
                data[i][5] = book.getDueDate();
                data[i][6] = book.getStudentId();
            }

            JTable issuedBooksTable = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(issuedBooksTable);
            JOptionPane.showMessageDialog(this, scrollPane, "الكتب المعارة", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في جلب البيانات: " + e.getMessage(), "خطأ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // استعراض الطلاب الذين استعاروا كتبًا
    private void handleViewStudentsWithBooks() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            BookDAO bookDAO = new BookDAO(connection);
            List<Student> students = bookDAO.getStudentsWithIssuedBooks();

            String[] columnNames = { "ID", "الاسم", "التخصص", "الفصل" };
            Object[][] data = new Object[students.size()][4];
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                data[i][0] = student.getId();
                data[i][1] = student.getName();
                data[i][2] = student.getCourse();
                data[i][3] = student.getSemester();
            }

            JTable studentsTable = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(studentsTable);
            JOptionPane.showMessageDialog(this, scrollPane, "الطلاب المستعيرين", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في جلب البيانات: " + e.getMessage(), "خطأ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // استعراض الطلاب الذين تجاوزوا موعد الإعادة
    private void handleViewOverdueBooks() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            BookDAO bookDAO = new BookDAO(connection);
            List<Student> students = bookDAO.getStudentsWithOverdueBooks();

            String[] columnNames = { "ID", "الاسم", "التخصص", "الفصل" };
            Object[][] data = new Object[students.size()][4];
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                data[i][0] = student.getId();
                data[i][1] = student.getName();
                data[i][2] = student.getCourse();
                data[i][3] = student.getSemester();
            }

            JTable overdueTable = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(overdueTable);
            JOptionPane.showMessageDialog(this, scrollPane, "الطلاب المتأخرين", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "فشل في جلب البيانات: " + e.getMessage(), "خطأ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}