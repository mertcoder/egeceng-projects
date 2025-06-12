package com.ege.reservation.ui;

import com.ege.reservation.data.User;
import com.ege.reservation.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuScreen extends Screen{
    private final UserService userService;

    private JButton searchVoyagesButton;
    private JButton myReservationsButton;
    private JButton myAccountButton;
    private JButton logoutButton;
    private JLabel welcomeLabel;


    public MainMenuScreen(UserService userService, UIFactory uiFactory){
        super(uiFactory);
        this.userService = userService;
        setTitle("Main Menu - Online Reservation System");
    }


    @Override
    protected JPanel createContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        // Create welcome panel
        JPanel welcomePanel = createWelcomePanel();

        // Create menu panel
        JPanel menuPanel = createMenuPanel();

        mainPanel.add(welcomePanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.CENTER);

        return mainPanel;
    }



    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        User currentUser = userService.getCurrentUser();
        String welcomeText = "Welcome, " + (currentUser != null ? currentUser.getFullName() : "User") + "!";

        welcomeLabel = new JLabel(welcomeText);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(52, 73, 94));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel subtitleLabel = new JLabel("What would you like to do today?");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);

        panel.add(welcomeLabel, BorderLayout.CENTER);
        panel.add(subtitleLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Search Voyages button
        gbc.gridx = 0; gbc.gridy = 0;
        searchVoyagesButton = createMenuButton(
                "Search Voyages",
                "Find and book your next journey",
                new Color(52, 152, 219),
                "🔍"
        );
        searchVoyagesButton.addActionListener(new SearchVoyagesActionListener());
        panel.add(searchVoyagesButton, gbc);

        // My Reservations button
        gbc.gridy = 1;
        myReservationsButton = createMenuButton(
                "My Reservations",
                "View and manage your bookings",
                new Color(46, 125, 50),
                "📋"
        );
        myReservationsButton.addActionListener(new MyReservationsActionListener());
        panel.add(myReservationsButton, gbc);

        // My Account button (placeholder)
        gbc.gridy = 2;
        myAccountButton = createMenuButton(
                "My Account",
                "Manage your account settings",
                new Color(156, 39, 176),
                "👤"
        );
        myAccountButton.addActionListener(new MyAccountActionListener());
        panel.add(myAccountButton, gbc);

        // Logout button
        gbc.gridy = 3;
        gbc.insets = new Insets(30, 20, 15, 20);
        logoutButton = createMenuButton(
                "Logout",
                "Sign out from your account",
                new Color(244, 67, 54),
                "🚪"
        );
        logoutButton.addActionListener(new LogoutActionListener());
        panel.add(logoutButton, gbc);

        return panel;
    }

    private JButton createMenuButton(String title, String description, Color bgColor, String icon) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(400, 80));

        // Icon and title panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        iconLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        topPanel.add(iconLabel);
        topPanel.add(titleLabel);

        // Description panel
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(new Color(255, 255, 255, 180));
        descLabel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));

        button.add(topPanel, BorderLayout.NORTH);
        button.add(descLabel, BorderLayout.CENTER);

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

    private class SearchVoyagesActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            VoyageSearchScreen voyageSearchScreen = uiFactory.createVoyageSearchScreen();
            navigateToScreen(voyageSearchScreen);
        }
    }

    private class MyReservationsActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            UserDashboardScreen userDashboardScreen = uiFactory.createUserDashboardScreen();
            navigateToScreen(userDashboardScreen);
        }
    }

    private class MyAccountActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            UserAccountScreen userAccountScreen = uiFactory.createUserAccountScreen();
            navigateToScreen(userAccountScreen);
        }
    }

    private class LogoutActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int option = JOptionPane.showConfirmDialog(
                    MainMenuScreen.this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (option == JOptionPane.YES_OPTION) {
                userService.logout();
                showSuccessMessage("Logged out successfully.");
                LoginScreen loginScreen = uiFactory.createLoginScreen();
                navigateToScreen(loginScreen);
            }
        }
    }


    @Override
    protected String getScreenTitle() {
        User currentUser = userService.getCurrentUser();
        return "Welcome " + (currentUser != null ? currentUser.getFullName() : "User");
    }
}

