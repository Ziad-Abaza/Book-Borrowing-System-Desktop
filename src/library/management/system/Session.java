package library.management.system;

public class Session {
    private static boolean loggedIn = false;
    private static String currentUserId = null;

    public static void login(String userId) {
        loggedIn = true;
        currentUserId = userId;
    }

    public static void logout() {
        loggedIn = false;
        currentUserId = null;
    }

    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static String getCurrentUserId() {
        return currentUserId;
    }
}