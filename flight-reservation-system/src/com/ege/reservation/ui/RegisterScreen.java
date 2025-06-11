package com.ege.reservation.ui;

import com.ege.reservation.service.UserService;
import com.ege.reservation.data.User;
import com.ege.reservation.util.InputValidator;

public class RegisterScreen extends Screen{
    private final UserService userService;

    public RegisterScreen(UserService userService, UIFactory uiFactory){
        super(uiFactory);
        this.userService = userService;
    }


    @Override
    protected void renderContent() {
        System.out.println("  REGISTER NEW ACCOUNT");
        System.out.println("----------------------------------------");
        System.out.println("Please enter your information:");
    }


    @Override
    protected void handleInput() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        if (!InputValidator.isValidUsername(username)) {
            System.out.println("⚠️  Username cannot be empty.");
            display();
            return;
        }

        String password;
        while (true) {
            System.out.print("Password: ");
            password = scanner.nextLine().trim();
            if (!InputValidator.isValidPassword(password)) {
                System.out.println("⚠️  Password must be at least 6 characters.");
            } else {
                break;
            }
        }

        System.out.print("Full Name: ");
        String fullName = scanner.nextLine().trim();
        if (!InputValidator.isNotEmpty(fullName)) {
            System.out.println("⚠️  Full name cannot be empty.");
            display();
            return;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (!InputValidator.isValidEmail(email)) {
            System.out.println("⚠️  Please enter a valid email address.");
            display();
            return;
        }

        User newUser = userService.registerUser(username, password, fullName, email);

        if (newUser != null) {
            System.out.println("Registration successful!");
            System.out.println("Please login with your new account.");
            uiFactory.createLoginScreen().display();
        } else {
            System.out.println("Username already exists. Please try again.");
            display();
        }
    }

}
