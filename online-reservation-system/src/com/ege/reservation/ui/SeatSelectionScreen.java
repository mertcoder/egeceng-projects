package com.ege.reservation.ui;

import com.ege.reservation.data.Seat;
import com.ege.reservation.data.Voyage;
import com.ege.reservation.service.ReservationService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Screen for selecting seats on a selected voyage
 */
public class SeatSelectionScreen extends Screen {
    private final String voyageId;
    private final ReservationService reservationService;
    private JTextField seatSelectionField;

    public SeatSelectionScreen(String voyageId, ReservationService reservationService, UIFactory uiFactory) {
        super(uiFactory);
        this.voyageId = voyageId;
        this.reservationService = reservationService;
        setTitle("Seat Selection - Online Reservation System");
    }

    @Override
    protected String getScreenTitle() {
        Voyage voyage = reservationService.getVoyageById(voyageId);
        if (voyage != null) {
            return "Select Seats - " + voyage.getTransport().getTransportName();
        }
        return "Select Seats";
    }

    @Override
    protected JPanel createContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Voyage voyage = reservationService.getVoyageById(voyageId);
        if (voyage == null) {
            JLabel errorLabel = new JLabel("Error: Voyage not found!");
            errorLabel.setForeground(Color.RED);
            errorLabel.setHorizontalAlignment(JLabel.CENTER);
            mainPanel.add(errorLabel, BorderLayout.CENTER);
            return mainPanel;
        }

        // Create voyage info panel
        JPanel voyageInfoPanel = createVoyageInfoPanel(voyage);

        // Create seat map panel
        JPanel seatMapPanel = createSeatMapPanel(voyage);

        // Create selection panel
        JPanel selectionPanel = createSelectionPanel();

        // Create buttons panel
        JPanel buttonsPanel = createButtonsPanel();

        mainPanel.add(voyageInfoPanel, BorderLayout.NORTH);
        mainPanel.add(seatMapPanel, BorderLayout.CENTER);
        mainPanel.add(selectionPanel, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(selectionPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createVoyageInfoPanel(Voyage voyage) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel(voyage.getTransport().getTransportName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(52, 73, 94));

        JLabel routeLabel = new JLabel(voyage.getOrigin() + " → " + voyage.getDestination());
        routeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        routeLabel.setForeground(new Color(127, 140, 141));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(routeLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSeatMapPanel(Voyage voyage) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Seat Map",
                0,
                0,
                new Font("Arial", Font.BOLD, 16),
                new Color(52, 73, 94)
        ));

        // Create seat map display
        JTextArea seatMapArea = new JTextArea();
        seatMapArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        seatMapArea.setEditable(false);
        seatMapArea.setBackground(new Color(248, 249, 250));

        List<String> availableSeats = reservationService.getAvailableSeats(voyageId);
        String seatMapText = generateSeatMapText(voyage, availableSeats);
        seatMapArea.setText(seatMapText);

        JScrollPane scrollPane = new JScrollPane(seatMapArea);
        scrollPane.setPreferredSize(new Dimension(600, 200));

        panel.add(scrollPane, BorderLayout.CENTER);

        // Add legend
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        legendPanel.setBackground(Color.WHITE);

        JLabel legendLabel = new JLabel("Legend: [ ] - Available, [X] - Reserved, [B] - Business, [E] - Emergency Exit, [H] - Handicap");
        legendLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        legendLabel.setForeground(new Color(127, 140, 141));

        legendPanel.add(legendLabel);
        panel.add(legendPanel, BorderLayout.SOUTH);

        return panel;
    }

    private String generateSeatMapText(Voyage voyage, List<String> availableSeats) {
        StringBuilder sb = new StringBuilder();
        int maxRows = 0;
        List<Seat> allSeats = voyage.getTransport().getSeats();

        // Find max row
        for (Seat seat : allSeats) {
            if (seat.getSeatRow() > maxRows) {
                maxRows = seat.getSeatRow();
            }
        }

        // Group seats by row
        for (int row = 1; row <= maxRows; row++) {
            sb.append(String.format("Row %2d: ", row));

            // Get all seats for this row
            List<Seat> rowSeats = new ArrayList<>();
            for (Seat seat : allSeats) {
                if (seat.getSeatRow() == row) {
                    rowSeats.add(seat);
                }
            }

            // Sort by column
            rowSeats.sort(Comparator.comparing(Seat::getSeatColumn));

            // Display seats
            for (Seat seat : rowSeats) {
                String marker;
                if (!availableSeats.contains(seat.getSeatId())) {
                    marker = "[X]"; // Reserved
                } else {
                    if (seat.getSeatType() == Seat.SeatType.BUSINESS) {
                        marker = "[B]";
                    } else {
                        marker = "[ ]";
                    }
                }
                sb.append(" ").append(seat.getSeatId()).append(marker);
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private JPanel createSelectionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel label = new JLabel("Enter seat IDs (comma-separated, e.g. 1A,1B,1C):");
        label.setFont(new Font("Arial", Font.BOLD, 14));

        seatSelectionField = new JTextField(20);
        seatSelectionField.setFont(new Font("Arial", Font.PLAIN, 14));
        seatSelectionField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        panel.add(label);
        panel.add(seatSelectionField);

        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(Color.WHITE);

        // Select Seats button
        JButton selectButton = createActionButton("Select Seats", new Color(46, 125, 50), "✅");
        selectButton.addActionListener(new SelectSeatsActionListener());

        // Back button
        JButton backButton = createActionButton("Back to Search Results", new Color(108, 117, 125), "⬅");
        backButton.addActionListener(new BackActionListener());

        panel.add(selectButton);
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

    private class SelectSeatsActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String seatInput = seatSelectionField.getText().trim();

            if (seatInput.isEmpty()) {
                showMessage("Please enter seat IDs.", "No Seats Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String[] selectedSeatIds = seatInput.split(",");

            // Validate seats
            boolean allSeatsValid = true;
            for (String seatId : selectedSeatIds) {
                String trimmedSeatId = seatId.trim();
                if (!reservationService.isSeatAvailable(voyageId, trimmedSeatId)) {
                    showMessage("Seat " + trimmedSeatId + " is not available. Please try again.", "Invalid Seat Selection", JOptionPane.ERROR_MESSAGE);
                    allSeatsValid = false;
                    break;
                }
            }

            if (allSeatsValid) {
                // Trim each seat ID
                for (int i = 0; i < selectedSeatIds.length; i++) {
                    selectedSeatIds[i] = selectedSeatIds[i].trim();
                }
                ReservationConfirmationScreen confirmationScreen = uiFactory.createReservationConfirmationScreen(voyageId, selectedSeatIds);
                navigateToScreen(confirmationScreen);
            }
        }
    }

    private class BackActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            VoyageSearchScreen searchScreen = uiFactory.createVoyageSearchScreen();
            navigateToScreen(searchScreen);
        }
    }
} 