package com.ege.reservation.ui;

import com.ege.reservation.service.UserService;
import com.ege.reservation.data.User;
import com.ege.reservation.util.InputValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterScreen extends Screen{
    private final UserService userService;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JButton registerButton;
    private JButton backButton;

    public RegisterScreen(UserService userService, UIFactory uiFactory) {
        super(uiFactory);
        this.userService = userService;
        setTitle("Register - Online Reservation System");
    }

    @Override
    protected String getScreenTitle() {
        return "Create New Account";
    }

    @Override
    protected JPanel createContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Create registration form
        JPanel formPanel = createFormPanel();

        // Create button panel
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Account Information",
                0,
                0,
                new Font("Arial", Font.BOLD, 16),
                new Color(52, 73, 94)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        usernameField = createTextField();
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(createLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        passwordField = createPasswordField();
        panel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(createLabel("Confirm Password:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        confirmPasswordField = createPasswordField();
        panel.add(confirmPasswordField, gbc);

        // Full Name
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(createLabel("Full Name:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        fullNameField = createTextField();
        panel.add(fullNameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(createLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        emailField = createTextField();
        panel.add(emailField, gbc);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(Color.WHITE);

        // Register button
        registerButton = new JButton("Create Account");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(46, 125, 50));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Back button
        backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add action listeners
        registerButton.addActionListener(new RegisterActionListener());
        backButton.addActionListener(new BackActionListener());

        // Add Enter key support
        emailField.addActionListener(new RegisterActionListener());

        panel.add(registerButton);
        panel.add(backButton);

        return panel;
    }

    private class RegisterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleRegistration();
        }
    }

    private class BackActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LoginScreen loginScreen = uiFactory.createLoginScreen();
            navigateToScreen(loginScreen);
        }
    }

    private void handleRegistration() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();

        // Validate fields
        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            showErrorMessage("Please fill in all fields.");
            return;
        }

        if (!InputValidator.isValidUsername(username)) {
            showErrorMessage("Username must be at least 3 characters long.");
            usernameField.requestFocus();
            return;
        }

        if (!InputValidator.isValidPassword(password)) {
            showErrorMessage("Password must be at least 6 characters long.");
            passwordField.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            showErrorMessage("Passwords do not match.");
            confirmPasswordField.setText("");
            passwordField.requestFocus();
            return;
        }

        if (!InputValidator.isValidEmail(email)) {
            showErrorMessage("Please enter a valid email address.");
            emailField.requestFocus();
            return;
        }

        // Show loading cursor
        setCursor(new Cursor(Cursor.WAIT_CURSOR));

        try {
            User newUser = userService.registerUser(username, password, fullName, email);

            if (newUser != null) {
                showSuccessMessage("Registration successful!\nPlease login with your new account.");
                LoginScreen loginScreen = uiFactory.createLoginScreen();
                navigateToScreen(loginScreen);
            } else {
                showErrorMessage("Username already exists. Please choose a different username.");
                usernameField.requestFocus();
                usernameField.selectAll();
            }
        } finally {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    @Override
    protected void onScreenDisplayed() {
        // Set focus to username field when screen is displayed
        SwingUtilities.invokeLater(() -> usernameField.requestFocus());
    }

}
