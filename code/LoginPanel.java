package code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Represents login panel in application's user interface.
 * Provides functionality for user login and registration, interacting
 * with ClientController to communicate with server.
 */
public class LoginPanel extends JPanel {

    private final JTextField usernameField; // Text field for entering username
    private final JPasswordField passwordField; // Password field for entering password
    private final JButton loginButton; // Button for initiating login
    private final JButton registerButton; // Button for initiating registration
    private final ClientController clientController; // Controller for managing client-server communication
    private final MainWindow mainWindow; // Reference to main application window for panel switching

    /**
     * Constructs LoginPanel with specified client controller and main window.
     *
     * @param clientController Controller for handling client-server interactions
     * @param mainWindow Main application window for switching panels
     */
    public LoginPanel(ClientController clientController, MainWindow mainWindow) {
        this.clientController = clientController;
        this.mainWindow = mainWindow;

        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255)); // Light blue background
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Welcome");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across 2 columns
        add(titleLabel, gbc);

        gbc.gridwidth = 1; // Reset to 1 column span
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Username:"), gbc);

        // Username input field
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Password:"), gbc);

        // Password input field
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;

        // Login button
        loginButton = new JButton("Login");
        styleButton(loginButton);
        add(loginButton, gbc);

        gbc.gridx = 1;

        // Register button
        registerButton = new JButton("Register");
        styleButton(registerButton);
        add(registerButton, gbc);

        // Action listeners for buttons
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegistration());
    }

    /**
     * Styles a JButton with custom appearance and hover effects, and applies
     * a consistent font, background color, text color and border to the provided JButton.
     *
     * @param button the JButton to be styled
     */
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180)); // Steel blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237)); // Lighter blue
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180)); // Original color
            }
        });
    }

    /**
     * Handles login process by sending username and password to server.
     * Displays success or error messages based on server's response.
     */
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            if (clientController.login(username, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                mainWindow.showPanel("Calendar"); // Switch to the calendar panel
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to server.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles registration process by sending username and password to server.
     * Displays success or error messages based on server's response.
     */
    private void handleRegistration() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            if (clientController.register(username, password)) {
                JOptionPane.showMessageDialog(this, "Registration successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Username may already be taken.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to server.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}