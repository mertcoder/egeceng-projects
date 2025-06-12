package com.ege.reservation.ui;

import com.ege.reservation.data.User;
import com.ege.reservation.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Screen for displaying and managing user account information
 */
public class UserAccountScreen extends Screen {
    private final UserService userService;
    
    public UserAccountScreen(UserService userService, UIFactory uiFactory) {
        super(uiFactory);
        this.userService = userService;
        setTitle("My Account - Online Reservation System");
    }
    
    @Override
    protected String getScreenTitle() {
        User currentUser = userService.getCurrentUser();
        return "My Account - " + (currentUser != null ? currentUser.getFullName() : "User");
    }
    
    @Override
    protected JPanel createContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create account info panel
        JPanel accountInfoPanel = createAccountInfoPanel();
        
        // Create buttons panel
        JPanel buttonsPanel = createButtonsPanel();
        
        mainPanel.add(accountInfoPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createAccountInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Account Information",
                0,
                0,
                new Font("Arial", Font.BOLD, 16),
                new Color(52, 73, 94)
        ));
        
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            JLabel errorLabel = new JLabel("Error: No user is currently logged in!");
            errorLabel.setForeground(Color.RED);
            errorLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(errorLabel, BorderLayout.CENTER);
            return panel;
        }
        
        // Create user info content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        // User ID
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(createLabel("User ID:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(createValueLabel(currentUser.getUserId()), gbc);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(createLabel("Username:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(createValueLabel(currentUser.getUsername()), gbc);
        
        // Full Name
        gbc.gridx = 0; gbc.gridy = 2;
        contentPanel.add(createLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(createValueLabel(currentUser.getFullName()), gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3;
        contentPanel.add(createLabel("Email:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(createValueLabel(currentUser.getEmail()), gbc);
        
        // Account Type
        gbc.gridx = 0; gbc.gridy = 4;
        contentPanel.add(createLabel("Account Type:"), gbc);
        gbc.gridx = 1;
        String accountType = currentUser.isAdmin() ? "Administrator" : "Regular User";
        JLabel accountTypeLabel = createValueLabel(accountType);
        if (currentUser.isAdmin()) {
            accountTypeLabel.setForeground(new Color(231, 76, 60));
            accountTypeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        }
        contentPanel.add(accountTypeLabel, gbc);
        
        // Add user avatar/icon
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 15, 10, 15);
        
        JLabel avatarLabel = new JLabel(currentUser.isAdmin() ? "👑" : "👤");
        avatarLabel.setFont(new Font("Arial", Font.PLAIN, 72));
        avatarLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(avatarLabel, gbc);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(52, 73, 94));
        return label;
    }
    
    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(44, 62, 80));
        return label;
    }
    
    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Edit Profile button (placeholder)
        JButton editButton = createActionButton("Edit Profile", new Color(52, 152, 219), "✏️");
        editButton.addActionListener(e -> showWarningMessage("Profile editing feature will be available in future updates."));
        
        // Change Password button (placeholder)
        JButton passwordButton = createActionButton("Change Password", new Color(155, 89, 182), "🔒");
        passwordButton.addActionListener(e -> showWarningMessage("Password change feature will be available in future updates."));
        
        // Back to Main Menu button
        JButton backButton = createActionButton("Back to Main Menu", new Color(108, 117, 125), "⬅");
        backButton.addActionListener(new BackActionListener());
        
        panel.add(editButton);
        panel.add(passwordButton);
        panel.add(backButton);
        
        return panel;
    }
    
    private JButton createActionButton(String text, Color bgColor, String icon) {
        JButton button = new JButton(icon + " " + text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private class BackActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MainMenuScreen mainMenuScreen = uiFactory.createMainMenuScreen();
            navigateToScreen(mainMenuScreen);
        }
    }
} 