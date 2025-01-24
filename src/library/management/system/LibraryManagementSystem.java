package library.management.system;

import library.management.system.ui.LoginUI;

public class LibraryManagementSystem {

    public static void main(String[] args) {
        // Launch the LoginUI when the application starts
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginUI loginUI = new LoginUI();
            loginUI.setVisible(true);
        });
    }
}