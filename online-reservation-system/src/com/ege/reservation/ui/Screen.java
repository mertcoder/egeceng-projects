package com.ege.reservation.ui;

import javax.swing.*;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Abstract base class for all UI screens using Swing (Template Method pattern)
 */
public abstract class Screen extends JFrame {
    protected final UIFactory uiFactory;
    protected final int WINDOW_WIDTH = 1200;
    protected final int WINDOW_HEIGHT = 750;

    public Screen(UIFactory uiFactory) {
        this.uiFactory = uiFactory;
        initializeFrame();
    }

    private void initializeFrame() {
        // Set window properties
        setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });
    }

    // Template method pattern
    public final void display() {
        // Clear previous content
        getContentPane().removeAll();

        // Create main layout
        setLayout(new BorderLayout());

        // Add components using template methods
        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        // Finalize display
        revalidate();
        repaint();
        setVisible(true);

        // Focus on content
        requestFocus();

        // Hook method for subclasses to perform additional setup
        onScreenDisplayed();
    }

    protected void onScreenDisplayed() {
        // does nothing by default,
        // subclasses can override this to perform additional setup.
    }

    protected JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Online Reservation System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel subtitleLabel = new JLabel(getScreenTitle());
        subtitleLabel.setForeground(new Color(149, 165, 166));
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        headerPanel.add(titlePanel, BorderLayout.CENTER);

        return headerPanel;
    }

    protected abstract JPanel createContent();

    protected abstract String getScreenTitle();

    protected JPanel createFooter() {
        JPanel footerPanel = new JPanel(new FlowLayout());
        footerPanel.setBackground(new Color(236, 240, 241));
        footerPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 40));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        JLabel footerLabel = new JLabel("© 2025 EGE University - AOOP Reservation Project");
        footerLabel.setForeground(new Color(52, 73, 94));
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        footerPanel.add(footerLabel);

        return footerPanel;
    }

    protected void handleWindowClosing() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit the application?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    protected void navigateToScreen(Screen screen) {
        this.setVisible(false);
        this.dispose();
        screen.display();
    }

    protected void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    protected void showErrorMessage(String message) {
        showMessage(message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void showSuccessMessage(String message) {
        showMessage(message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void showWarningMessage(String message) {
        showMessage(message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}