package com.ege.reservation.ui;

import com.ege.reservation.data.User;
import com.ege.reservation.reservation.Reservation;
import com.ege.reservation.service.ReservationService;
import com.ege.reservation.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Dashboard screen for viewing and managing user reservations
 */
public class UserDashboardScreen extends Screen {
    private final UserService userService;
    private final ReservationService reservationService;

    public UserDashboardScreen(UserService userService, ReservationService reservationService, UIFactory uiFactory) {
        super(uiFactory);
        this.userService = userService;
        this.reservationService = reservationService;
        setTitle("My Reservations - Online Reservation System");
    }

    @Override
    protected String getScreenTitle() {
        User currentUser = userService.getCurrentUser();
        return "My Reservations - " + (currentUser != null ? currentUser.getFullName() : "User");
    }

    @Override
    protected JPanel createContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create reservations panel
        JPanel reservationsPanel = createReservationsPanel();

        // Create buttons panel
        JPanel buttonsPanel = createButtonsPanel();

        mainPanel.add(reservationsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createReservationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Your Reservations",
                0,
                0,
                new Font("Arial", Font.BOLD, 16),
                new Color(52, 73, 94)
        ));

        User currentUser = userService.getCurrentUser();
        List<Reservation> userReservations = reservationService.getUserReservations(currentUser.getUserId());

        if (userReservations.isEmpty()) {
            JLabel noReservationsLabel = new JLabel("You don't have any reservations yet.");
            noReservationsLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            noReservationsLabel.setForeground(new Color(127, 140, 141));
            noReservationsLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(noReservationsLabel, BorderLayout.CENTER);
        } else {
            // Create reservations list
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (int i = 0; i < userReservations.size(); i++) {
                Reservation reservation = userReservations.get(i);
                String reservationInfo = String.format("%d. %s (%s -> %s) - Status: %s - Seats: %s",
                        (i + 1),
                        reservation.getVoyage().getTransport().getTransportName(),
                        reservation.getVoyage().getOrigin(),
                        reservation.getVoyage().getDestination(),
                        reservation.getStatus(),
                        String.join(", ", reservation.getSeatIds())
                );
                listModel.addElement(reservationInfo);
            }

            JList<String> reservationsList = new JList<>(listModel);
            reservationsList.setFont(new Font("Courier New", Font.PLAIN, 12));
            reservationsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scrollPane = new JScrollPane(reservationsList);
            scrollPane.setPreferredSize(new Dimension(600, 300));

            panel.add(scrollPane, BorderLayout.CENTER);
        }

        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(Color.WHITE);

        // Cancel Reservation button
        JButton cancelButton = createActionButton("Cancel a Reservation", new Color(244, 67, 54), "❌");
        cancelButton.addActionListener(e -> showMessage("Cancel reservation feature will be implemented soon.", "Feature Coming Soon", JOptionPane.INFORMATION_MESSAGE));

        // Back to Main Menu button
        JButton backButton = createActionButton("Back to Main Menu", new Color(108, 117, 125), "⬅");
        backButton.addActionListener(new BackActionListener());

        panel.add(cancelButton);
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