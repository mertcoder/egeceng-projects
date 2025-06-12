package com.ege.reservation.ui;

import com.ege.reservation.data.User;
import com.ege.reservation.data.Voyage;
import com.ege.reservation.reservation.Reservation;
import com.ege.reservation.service.ReservationService;
import com.ege.reservation.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Screen for confirming and finalizing reservations
 */
public class ReservationConfirmationScreen extends Screen {
    private final String voyageId;
    private final String[] selectedSeatIds;
    private final ReservationService reservationService;
    private final UserService userService;



    public ReservationConfirmationScreen(String voyageId, String[] selectedSeatIds,
                                         ReservationService reservationService,
                                         UserService userService, UIFactory uiFactory) {
        super(uiFactory);
        this.voyageId = voyageId;
        this.selectedSeatIds = selectedSeatIds;
        this.reservationService = reservationService;
        this.userService = userService;
        setTitle("Reservation Confirmation - Online Reservation System");
    }

    @Override
    protected String getScreenTitle() {
        return "Confirm Your Reservation";
    }

    @Override
    protected JPanel createContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create voyage details panel
        JPanel detailsPanel = createVoyageDetailsPanel();

        // Create buttons panel
        JPanel buttonsPanel = createButtonsPanel();

        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createVoyageDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Reservation Details",
                0,
                0,
                new Font("Arial", Font.BOLD, 16),
                new Color(52, 73, 94)
        ));

        Voyage voyage = reservationService.getVoyageById(voyageId);
        if (voyage == null) {
            JLabel errorLabel = new JLabel("Error: Voyage not found!");
            errorLabel.setForeground(Color.RED);
            errorLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(errorLabel, BorderLayout.CENTER);
            return panel;
        }

        // Create details content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Voyage details
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(createLabel("Voyage:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(createValueLabel(voyage.getTransport().getTransportName()), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(createLabel("Route:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(createValueLabel(voyage.getOrigin() + " → " + voyage.getDestination()), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        contentPanel.add(createLabel("Departure:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(createValueLabel(voyage.getDepartureTime().format(formatter)), gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        contentPanel.add(createLabel("Arrival:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(createValueLabel(voyage.getArrivalTime().format(formatter)), gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        contentPanel.add(createLabel("Selected Seats:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(createValueLabel(String.join(", ", selectedSeatIds)), gbc);

        double totalPrice = reservationService.calculatePrice(voyageId, Arrays.asList(selectedSeatIds));
        gbc.gridx = 0; gbc.gridy = 5;
        contentPanel.add(createLabel("Total Price:"), gbc);
        gbc.gridx = 1;
        JLabel priceLabel = createValueLabel(String.format("$%.2f", totalPrice));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setForeground(new Color(46, 125, 50));
        contentPanel.add(priceLabel, gbc);

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

        // Confirm button
        JButton confirmButton = createActionButton("Confirm and Create Reservation", new Color(46, 125, 50), "✅");
        confirmButton.addActionListener(new ConfirmActionListener());

        // Back button
        JButton backButton = createActionButton("Back to Seat Selection", new Color(108, 117, 125), "⬅");
        backButton.addActionListener(new BackActionListener());

        panel.add(confirmButton);
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

    private class ConfirmActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            User currentUser = userService.getCurrentUser();
            List<String> seatList = Arrays.asList(selectedSeatIds);

            Reservation reservation = reservationService.createReservation(currentUser, voyageId, seatList);

            if (reservation != null) {
                String successMessage = String.format(
                        "Reservation successful!\n\n" +
                                "Reservation ID: %s\n" +
                                "Voyage: %s\n" +
                                "Route: %s → %s\n" +
                                "Seats: %s\n" +
                                "Total Price: $%.2f",
                        reservation.getId(),
                        reservation.getVoyage().getTransport().getTransportName(),
                        reservation.getVoyage().getOrigin(),
                        reservation.getVoyage().getDestination(),
                        String.join(", ", reservation.getSeatIds()),
                        reservation.getTotalPrice()
                );

                showMessage(successMessage, "Reservation Successful", JOptionPane.INFORMATION_MESSAGE);

                MainMenuScreen mainMenuScreen = uiFactory.createMainMenuScreen();
                navigateToScreen(mainMenuScreen);
            } else {
                showMessage("Failed to create reservation. Please try again.", "Reservation Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class BackActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SeatSelectionScreen seatSelectionScreen = uiFactory.createSeatSelectionScreen(voyageId);
            navigateToScreen(seatSelectionScreen);
        }
    }
} 