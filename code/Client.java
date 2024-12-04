package code;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import javax.swing.*;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Client client;
    private MainWindow mainWindow;
    private HashMap<String, String> userDatabase; // Simulated user database

    public LoginPanel(Client client, MainWindow mainWindow) {
        this.client = client;
        this.mainWindow = mainWindow;
        this.userDatabase = new HashMap<>(); // Initialize in-memory user database

        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255)); // Light blue background
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding

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

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;

        JButton loginButton = new JButton("Login");
        styleButton(loginButton);
        loginButton.addActionListener(this::handleLogin);
        add(loginButton, gbc);

        gbc.gridx = 1;
        JButton registerButton = new JButton("Register");
        styleButton(registerButton);
        registerButton.addActionListener(this::handleRegister);
        add(registerButton, gbc);
    }

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

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            mainWindow.switchToCalendar(username); // Assume mainWindow switches to calendar
        } else {
            JOptionPane.showMessageDialog(this, "Login failed. Check your credentials.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userDatabase.containsKey(username)) {
            JOptionPane.showMessageDialog(this, "Registration failed. Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            userDatabase.put(username, password);
            JOptionPane.showMessageDialog(this, "Registration successful! You can now log in.");
        }
    }
}
