package com.ege.reservation.util;

public class InputValidator {

    /**
     * Checks if a password is valid.
     * @param password the password to check
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Checks if a username is valid (not empty).
     */
    public static boolean isValidUsername(String username) {
        return username != null && username.trim().length() >= 3;
    }

    // İstersen e-mail, isim, koltuk ID kontrolü gibi diğer validasyonları da ekleyebilirsin
    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    public static boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }
}