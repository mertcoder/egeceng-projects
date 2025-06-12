package com.ege.reservation.ui;

import com.ege.reservation.data.Voyage;
import com.ege.reservation.service.ReservationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Screen for searching and selecting voyages using Swing
 */
public class VoyageSearchScreen extends Screen {
    private final ReservationService reservationService;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private JTextField originField;
    private JTextField destinationField;
    private JSpinner dateSpinner;
    private JButton searchButton;
    private JButton listAllButton;
    private JButton backButton;
    private JTable voyageTable;
    private DefaultTableModel tableModel;
    private JButton selectButton;
    private JLabel statusLabel;
    private List<Voyage> currentVoyages;

    public VoyageSearchScreen(ReservationService reservationService, UIFactory uiFactory) {
        super(uiFactory);
        this.reservationService = reservationService;
        setTitle("Search Voyages - Online Reservation System");
    }

    @Override
    protected String getScreenTitle() {
        return "Search for Your Journey";
    }

    @Override
    protected JPanel createContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create search form panel
        JPanel searchPanel = createSearchPanel();

        // Create results panel
        JPanel resultsPanel = createResultsPanel();

        // Create button panel
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(resultsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Search Criteria",
                0,
                0,
                new Font("Arial", Font.BOLD, 16),
                new Color(52, 73, 94)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Origin
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createLabel("From:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        originField = createSearchField("e.g., Istanbul");
        panel.add(originField, gbc);

        // Destination
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(createLabel("To:"), gbc);

        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        destinationField = createSearchField("e.g., Ankara");
        panel.add(destinationField, gbc);

        // Date
        gbc.gridx = 4; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(createLabel("Date:"), gbc);

        gbc.gridx = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        dateSpinner = createDateSpinner();
        panel.add(dateSpinner, gbc);

        // Search button
        gbc.gridx = 6; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        searchButton = new JButton("🔍 Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setBackground(new Color(52, 152, 219));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(new SearchActionListener());
        panel.add(searchButton, gbc);

        // List All button
        gbc.gridx = 7; gbc.gridy = 0;
        gbc.insets = new Insets(8, 5, 8, 10); // Closer to search button
        listAllButton = new JButton("📋 List All");
        listAllButton.setFont(new Font("Arial", Font.BOLD, 14));
        listAllButton.setBackground(new Color(155, 89, 182));
        listAllButton.setForeground(Color.WHITE);
        listAllButton.setFocusPainted(false);
        listAllButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        listAllButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        listAllButton.addActionListener(new ListAllActionListener());
        panel.add(listAllButton, gbc);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private JTextField createSearchField(String placeholder) {
        JTextField field = new JTextField(15);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        // Add placeholder behavior
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });

        return field;
    }

    private JSpinner createDateSpinner() {
        // Set initial date to today
        Date currentDate = new Date();

        // Create spinner with date model
        SpinnerDateModel dateModel = new SpinnerDateModel(currentDate, null, null, Calendar.DAY_OF_MONTH);
        JSpinner spinner = new JSpinner(dateModel);

        // Format spinner to show only date (not time)
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd");
        spinner.setEditor(dateEditor);

        // Style the spinner
        spinner.setFont(new Font("Arial", Font.PLAIN, 14));
        spinner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        return spinner;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Search Results",
                0,
                0,
                new Font("Arial", Font.BOLD, 16),
                new Color(52, 73, 94)
        ));

        // Create table
        String[] columnNames = {"Transport", "Type", "Route", "Departure", "Arrival", "Price", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        voyageTable = new JTable(tableModel);
        voyageTable.setFont(new Font("Arial", Font.PLAIN, 12));
        voyageTable.setRowHeight(30);
        voyageTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        voyageTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        voyageTable.getTableHeader().setBackground(new Color(52, 73, 94));
        voyageTable.getTableHeader().setForeground(Color.WHITE);

        // Add double-click listener
        voyageTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    handleVoyageSelection();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(voyageTable);
        scrollPane.setPreferredSize(new Dimension(750, 300));

        // Status label
        statusLabel = new JLabel("Enter search criteria and click Search, or click 'List All' to see all voyages.");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setForeground(new Color(127, 140, 141));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBackground(Color.WHITE);

        // Select voyage button
        selectButton = new JButton("Select Voyage");
        selectButton.setFont(new Font("Arial", Font.BOLD, 14));
        selectButton.setBackground(new Color(46, 125, 50));
        selectButton.setForeground(Color.WHITE);
        selectButton.setFocusPainted(false);
        selectButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        selectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        selectButton.setEnabled(false);
        selectButton.addActionListener(e -> handleVoyageSelection());

        // Back button
        backButton = new JButton("Back to Main Menu");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(new BackActionListener());

        panel.add(selectButton);
        panel.add(backButton);

        return panel;
    }

    private class SearchActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleSearch();
        }
    }

    private class BackActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MainMenuScreen mainMenuScreen = uiFactory.createMainMenuScreen();
            navigateToScreen(mainMenuScreen);
        }
    }

    private class ListAllActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleListAll();
        }
    }

    private void handleSearch() {
        String origin = getFieldText(originField, "e.g., Istanbul");
        String destination = getFieldText(destinationField, "e.g., Ankara");

        if (origin.isEmpty() || destination.isEmpty()) {
            showErrorMessage("Please fill in all search fields.");
            return;
        }

        // Get selected date from spinner
        Date selectedDate = (Date) dateSpinner.getValue();
        LocalDateTime date = selectedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .withHour(0)
                .withMinute(0)
                .withSecond(0);

        // Show loading cursor
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        statusLabel.setText("Searching voyages...");

        try {
            currentVoyages = reservationService.searchVoyages(origin, destination, date);
            displaySearchResults(currentVoyages);
        } finally {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void handleListAll() {
        // Show loading cursor
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        statusLabel.setText("Loading all voyages...");

        try {
            // Get all voyages from service
            currentVoyages = getAllVoyagesFromService();

            if (currentVoyages.isEmpty()) {
                statusLabel.setText("No voyages available in the system.");
                tableModel.setRowCount(0);
                selectButton.setEnabled(false);
            } else {
                statusLabel.setText("Showing all " + currentVoyages.size() + " voyage(s) in the system. Double-click a row or select and click 'Select Voyage'.");
                displaySearchResults(currentVoyages);
            }
        } catch (Exception ex) {
            showErrorMessage("Error loading voyages: " + ex.getMessage());
            statusLabel.setText("Error occurred while loading voyages.");
        } finally {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private List<Voyage> getAllVoyagesFromService() {
        return reservationService.getAllVoyages();
    }

    private String getFieldText(JTextField field, String placeholder) {
        String text = field.getText().trim();
        return text.equals(placeholder) ? "" : text;
    }

    private void displaySearchResults(List<Voyage> voyages) {
        // Clear previous results
        tableModel.setRowCount(0);

        if (voyages.isEmpty()) {
            statusLabel.setText("No voyages found matching your criteria. Try different search terms.");
            selectButton.setEnabled(false);
        } else {
            statusLabel.setText("Found " + voyages.size() + " voyage(s). Double-click a row or select and click 'Select Voyage'.");

            // Add voyages to table
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (Voyage voyage : voyages) {
                Object[] row = {
                        voyage.getTransport().getTransportName(),
                        voyage.getTransport().getTransportType().toString().toLowerCase(),
                        voyage.getOrigin() + " → " + voyage.getDestination(),
                        voyage.getDepartureTime().format(formatter),
                        voyage.getArrivalTime().format(formatter),
                        String.format("$%.2f", voyage.getPrice()),
                        voyage.getStatus().toString()
                };
                tableModel.addRow(row);
            }

            selectButton.setEnabled(true);
        }
    }

    private void handleVoyageSelection() {
        int selectedRow = voyageTable.getSelectedRow();

        if (selectedRow == -1) {
            showWarningMessage("Please select a voyage from the table.");
            return;
        }

        if (currentVoyages == null || selectedRow >= currentVoyages.size()) {
            showErrorMessage("Invalid voyage selection.");
            return;
        }

        Voyage selectedVoyage = currentVoyages.get(selectedRow);
        SeatSelectionScreen seatSelectionScreen = uiFactory.createSeatSelectionScreen(selectedVoyage.getId());
        navigateToScreen(seatSelectionScreen);
    }

    @Override
    protected void onScreenDisplayed() {
        // Set focus to origin field when screen is displayed
        SwingUtilities.invokeLater(() -> {
            originField.requestFocus();
            if (originField.getText().equals("e.g, Izmir")) {
                originField.selectAll();
            }
        });
    }
} 