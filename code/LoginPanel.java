package code;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.GridLayout;
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

        setLayout(new GridLayout(3, 2, 10, 10));

        // Username input field
        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        // Password input field
        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        // Login button
        loginButton = new JButton("Login");
        add(loginButton);

        // Register button
        registerButton = new JButton("Register");
        add(registerButton);

        // Action listeners for buttons
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegistration());
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
