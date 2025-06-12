package com.ege.reservation.ui;

import com.ege.reservation.data.TransportType;
import com.ege.reservation.data.User;
import com.ege.reservation.data.Voyage;
import com.ege.reservation.service.UserService;
import com.ege.reservation.service.VoyageService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*
  Dashboard screen for admin users to manage voyages
 */
public class AdminDashboardScreen extends Screen {
    private final UserService userService;
    private final VoyageService voyageService;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public AdminDashboardScreen(UserService userService, VoyageService voyageService, UIFactory uiFactory) {
        super(uiFactory);
        this.userService = userService;
        this.voyageService = voyageService;
    }

    protected String getScreenTitle() {
        User admin = userService.getCurrentUser();
        return "Admin Dashboard - " + (admin != null ? admin.getFullName() : "Administrator");
    }


    @Override
    protected JPanel createContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create voyages list panel
        JPanel voyagesPanel = createVoyagesPanel();

        // Create buttons panel
        JPanel buttonsPanel = createButtonsPanel();

        mainPanel.add(voyagesPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return mainPanel;
    }


    private JPanel createVoyagesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "All Voyages",
                0,
                0,
                new Font("Arial", Font.BOLD, 16),
                new Color(52, 73, 94)
        ));

        // Create voyages list
        DefaultListModel<String> listModel = new DefaultListModel<>();
        List<Voyage> allVoyages = voyageService.getAllVoyages();

        if (allVoyages.isEmpty()) {
            listModel.addElement("No voyages available...");
        } else {
            for (int i = 0; i < allVoyages.size(); i++) {
                Voyage voyage = allVoyages.get(i);
                listModel.addElement((i + 1) + ". " + voyage.toString());
            }
        }

        JList<String> voyagesList = new JList<>(listModel);
        voyagesList.setFont(new Font("Courier New", Font.PLAIN, 12));
        voyagesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(voyagesList);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }



    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(Color.WHITE);

        // Add Voyage button
        JButton addButton = createActionButton("Add New Voyage", new Color(46, 125, 50), "➕");
        addButton.addActionListener(new AddVoyageActionListener());

        // Cancel Voyage button
        JButton cancelButton = createActionButton("Cancel Voyage", new Color(255, 193, 7), "⏸");
        cancelButton.addActionListener(new CancelVoyageActionListener());

        // Delete Voyage button
        JButton deleteButton = createActionButton("Delete Voyage", new Color(244, 67, 54), "🗑");
        deleteButton.addActionListener(new DeleteVoyageActionListener());

        // Logout button
        JButton logoutButton = createActionButton("Logout", new Color(108, 117, 125), "🚪");
        logoutButton.addActionListener(new LogoutActionListener());

        panel.add(addButton);
        panel.add(cancelButton);
        panel.add(deleteButton);
        panel.add(logoutButton);

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

        // implemented hover effect for better ui experience.
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

    private class AddVoyageActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleAddVoyage();
        }
    }

    private class CancelVoyageActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleCancelVoyage();
        }
    }

    private class DeleteVoyageActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleDeleteVoyage();
        }
    }


    private class LogoutActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int option = JOptionPane.showConfirmDialog(
                    AdminDashboardScreen.this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (option == JOptionPane.YES_OPTION) {
                userService.logout();
                showMessage("Logged out successfully.", "Logout", JOptionPane.INFORMATION_MESSAGE);
                LoginScreen loginScreen = uiFactory.createLoginScreen();
                navigateToScreen(loginScreen);
            }
        }
    }

    private void handleAddVoyage() {
        AddVoyageDialog dialog = new AddVoyageDialog(this);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            VoyageData data = dialog.getVoyageData();

            try {
                Voyage newVoyage = voyageService.addVoyage(
                        data.transportType,
                        data.transportName,
                        data.companyName,
                        data.transportNumber,
                        data.origin,
                        data.destination,
                        data.departureTime,
                        data.arrivalTime,
                        data.price
                );

                if (newVoyage != null) {
                    showSuccessMessage("Voyage added successfully!");
                    refreshVoyagesList();
                } else {
                    showErrorMessage("Failed to add voyage. Please try again.");
                }
            } catch (Exception ex) {
                showErrorMessage("Error adding voyage: " + ex.getMessage());
            }
        }
    }

    private void handleCancelVoyage() {
        List<Voyage> allVoyages = voyageService.getAllVoyages();
        if (allVoyages.isEmpty()) {
            showWarningMessage("No voyages available to cancel.");
            return;
        }

        // Create voyage selection dialog
        String[] voyageOptions = new String[allVoyages.size()];
        for (int i = 0; i < allVoyages.size(); i++) {
            Voyage voyage = allVoyages.get(i);
            voyageOptions[i] = voyage.getTransport().getTransportName() + " (" + voyage.getOrigin() + " → " + voyage.getDestination() + ")";
        }

        String selected = (String) JOptionPane.showInputDialog(
                this,
                "Select a voyage to cancel:",
                "Cancel Voyage",
                JOptionPane.QUESTION_MESSAGE,
                null,
                voyageOptions,
                voyageOptions[0]
        );

        if (selected != null) {
            int selectedIndex = -1;
            for (int i = 0; i < voyageOptions.length; i++) {
                if (voyageOptions[i].equals(selected)) {
                    selectedIndex = i;
                    break;
                }
            }

            if (selectedIndex >= 0) {
                Voyage selectedVoyage = allVoyages.get(selectedIndex);
                boolean cancelled = voyageService.cancelVoyage(selectedVoyage.getId());

                if (cancelled) {
                    showSuccessMessage("Voyage cancelled successfully.");
                    refreshVoyagesList();
                } else {
                    showErrorMessage("Failed to cancel voyage.");
                }
            }
        }
    }

    private void handleDeleteVoyage() {
        List<Voyage> allVoyages = voyageService.getAllVoyages();
        if (allVoyages.isEmpty()) {
            showWarningMessage("No voyages available to delete.");
            return;
        }

        // Create voyage selection dialog
        String[] voyageOptions = new String[allVoyages.size()];
        for (int i = 0; i < allVoyages.size(); i++) {
            Voyage voyage = allVoyages.get(i);
            voyageOptions[i] = voyage.getTransport().getTransportName() + " (" + voyage.getOrigin() + " → " + voyage.getDestination() + ")";
        }

        String selected = (String) JOptionPane.showInputDialog(
                this,
                "Select a voyage to delete:",
                "Delete Voyage",
                JOptionPane.QUESTION_MESSAGE,
                null,
                voyageOptions,
                voyageOptions[0]
        );

        if (selected != null) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this voyage?\nThis action cannot be undone.",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int selectedIndex = -1;
                for (int i = 0; i < voyageOptions.length; i++) {
                    if (voyageOptions[i].equals(selected)) {
                        selectedIndex = i;
                        break;
                    }
                }

                if (selectedIndex >= 0) {
                    Voyage selectedVoyage = allVoyages.get(selectedIndex);
                    boolean deleted = voyageService.deleteVoyage(selectedVoyage.getId());

                    if (deleted) {
                        showSuccessMessage("Voyage deleted successfully.");
                        refreshVoyagesList();
                    } else {
                        showErrorMessage("Failed to delete voyage.");
                    }
                }
            }
        }
    }

    private void refreshVoyagesList() {
        // Refresh the ui
        this.getContentPane().removeAll();
        this.setLayout(new BorderLayout());
        this.add(createHeader(), BorderLayout.NORTH);
        this.add(createContent(), BorderLayout.CENTER);
        this.add(createFooter(), BorderLayout.SOUTH);
        this.revalidate();
        this.repaint();
    }

    // Inner class for voyage data
    private static class VoyageData {
        TransportType transportType;
        String transportName;
        String companyName;
        String transportNumber;
        String origin;
        String destination;
        LocalDateTime departureTime;
        LocalDateTime arrivalTime;
        double price;
    }

    // Inner class for Add Voyage Dialog
    private static class AddVoyageDialog extends JDialog {
        private VoyageData voyageData;
        private boolean confirmed = false;

        private JComboBox<TransportType> transportTypeCombo;
        private JTextField transportNameField;
        private JTextField companyNameField;
        private JTextField transportNumberField;
        private JTextField originField;
        private JTextField destinationField;
        private JSpinner departureTimeSpinner;
        private JSpinner arrivalTimeSpinner;
        private JTextField priceField;

        public AddVoyageDialog(JFrame parent) {
            super(parent, "Add New Voyage", true);
            initializeDialog();
        }

        private void initializeDialog() {
            setSize(400, 500);
            setLocationRelativeTo(getOwner());
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            JPanel mainPanel = new JPanel(new BorderLayout());

            // Form panel
            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.WEST;

            // Transport Type
            gbc.gridx = 0;
            gbc.gridy = 0;
            formPanel.add(new JLabel("Transport Type:"), gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            transportTypeCombo = new JComboBox<>(TransportType.values());
            formPanel.add(transportTypeCombo, gbc);

            // Transport Name
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.fill = GridBagConstraints.NONE;
            formPanel.add(new JLabel("Transport Name:"), gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            transportNameField = new JTextField(20);
            formPanel.add(transportNameField, gbc);

            // Company Name
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.fill = GridBagConstraints.NONE;
            formPanel.add(new JLabel("Company Name:"), gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            companyNameField = new JTextField(20);
            formPanel.add(companyNameField, gbc);

            // Transport Number
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.fill = GridBagConstraints.NONE;
            formPanel.add(new JLabel("Transport Number:"), gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            transportNumberField = new JTextField(20);
            formPanel.add(transportNumberField, gbc);

            // Origin
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.fill = GridBagConstraints.NONE;
            formPanel.add(new JLabel("Origin:"), gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            originField = new JTextField(20);
            formPanel.add(originField, gbc);

            // Destination
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.fill = GridBagConstraints.NONE;
            formPanel.add(new JLabel("Destination:"), gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            destinationField = new JTextField(20);
            formPanel.add(destinationField, gbc);

            // Departure Time
            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.fill = GridBagConstraints.NONE;
            formPanel.add(new JLabel("Departure Date & Time:"), gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            departureTimeSpinner = createDateTimeSpinner();
            formPanel.add(departureTimeSpinner, gbc);

            // Arrival Time
            gbc.gridx = 0;
            gbc.gridy = 7;
            gbc.fill = GridBagConstraints.NONE;
            formPanel.add(new JLabel("Arrival Date & Time:"), gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            arrivalTimeSpinner = createDateTimeSpinner();
            formPanel.add(arrivalTimeSpinner, gbc);

            // Price
            gbc.gridx = 0;
            gbc.gridy = 8;
            gbc.fill = GridBagConstraints.NONE;
            formPanel.add(new JLabel("Price:"), gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            priceField = new JTextField(20);
            formPanel.add(priceField, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton confirmButton = new JButton("Add Voyage");
            JButton cancelButton = new JButton("Cancel");

            confirmButton.addActionListener(e -> {
                if (validateAndCollectData()) {
                    confirmed = true;
                    dispose();
                }
            });

            cancelButton.addActionListener(e -> dispose());

            buttonPanel.add(confirmButton);
            buttonPanel.add(cancelButton);

            mainPanel.add(formPanel, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            add(mainPanel);
        }

        private JSpinner createDateTimeSpinner() {
            // Set initial time to current time + 1 hour
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            Date initialDate = calendar.getTime();

            // Create spinner with date model
            SpinnerDateModel dateModel = new SpinnerDateModel(initialDate, null, null, Calendar.MINUTE);
            JSpinner spinner = new JSpinner(dateModel);

            // Format spinner to show date and time
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd HH:mm");
            spinner.setEditor(dateEditor);

            // Style the spinner
            spinner.setFont(new Font("Arial", Font.PLAIN, 12));

            return spinner;
        }

        private boolean validateAndCollectData() {
            try {
                voyageData = new VoyageData();
                voyageData.transportType = (TransportType) transportTypeCombo.getSelectedItem();
                voyageData.transportName = transportNameField.getText().trim();
                voyageData.companyName = companyNameField.getText().trim();
                voyageData.transportNumber = transportNumberField.getText().trim();
                voyageData.origin = originField.getText().trim();
                voyageData.destination = destinationField.getText().trim();

                if (voyageData.transportName.isEmpty() || voyageData.companyName.isEmpty() ||
                        voyageData.origin.isEmpty() || voyageData.destination.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                // Get dates from spinners and convert to LocalDateTime
                Date departureDate = (Date) departureTimeSpinner.getValue();
                Date arrivalDate = (Date) arrivalTimeSpinner.getValue();

                voyageData.departureTime = departureDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                voyageData.arrivalTime = arrivalDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                if (voyageData.arrivalTime.isBefore(voyageData.departureTime)) {
                    JOptionPane.showMessageDialog(this, "Arrival time cannot be before departure time.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                voyageData.price = Double.parseDouble(priceField.getText().trim());
                if (voyageData.price <= 0) {
                    JOptionPane.showMessageDialog(this, "Price must be positive.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                return true;
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd HH:mm", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid price format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        public boolean isConfirmed() {
            return confirmed;
        }

        public VoyageData getVoyageData() {
            return voyageData;
        }
    }
}
