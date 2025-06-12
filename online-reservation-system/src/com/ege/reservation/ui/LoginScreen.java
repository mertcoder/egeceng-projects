package com.ege.reservation.ui;

import com.ege.reservation.data.User;
import com.ege.reservation.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login screen implementation using Swing
 */
public class LoginScreen extends Screen {
    private final UserService userService;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton exitButton;

    public LoginScreen(UserService userService, UIFactory uiFactory) {
        super(uiFactory);
        this.userService = userService;
        setTitle("Login - Online Reservation System");
    }

    @Override
    protected String getScreenTitle() {
        return "Welcome! Please login to continue";
    }

    @Override
    protected JPanel createContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Create center login form
        JPanel loginPanel = createLoginPanel();

        // Create button panel
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(loginPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Login Credentials",
                0,
                0,
                new Font("Arial", Font.BOLD, 16),
                new Color(52, 73, 94)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Username label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panel.add(usernameField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panel.add(passwordField, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(Color.WHITE);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(46, 125, 50));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Register button
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(33, 150, 243));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Exit button
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setBackground(new Color(244, 67, 54));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add action listeners
        loginButton.addActionListener(new LoginActionListener());
        registerButton.addActionListener(new RegisterActionListener());
        exitButton.addActionListener(new ExitActionListener());

        // Add Enter key support for login
        passwordField.addActionListener(new LoginActionListener());
        usernameField.addActionListener(e -> passwordField.requestFocus());

        panel.add(loginButton);
        panel.add(registerButton);
        panel.add(exitButton);

        return panel;
    }

    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleLogin();
        }
    }

    private class RegisterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            RegisterScreen registerScreen = uiFactory.createRegisterScreen();
            navigateToScreen(registerScreen);
        }
    }

    private class ExitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleWindowClosing();
        }
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            showErrorMessage("Please enter both username and password.");
            return;
        }

        // Show loading cursor
        setCursor(new Cursor(Cursor.WAIT_CURSOR));

        try {
            User user = userService.login(username, password);

            if (user != null) {
                showSuccessMessage("Login successful. Welcome, " + user.getFullName() + "!");

                // Navigate to appropriate dashboard based on user role
                Screen nextScreen;
                if (user.isAdmin()) {
                    nextScreen = uiFactory.createAdminDashboardScreen();
                } else {
                    nextScreen = uiFactory.createMainMenuScreen();
                }
                navigateToScreen(nextScreen);
            } else {
                showErrorMessage("Invalid username or password. Please try again.");
                passwordField.setText("");
                usernameField.requestFocus();
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