package code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;
    private final ClientController clientController;
    private final MainWindow mainWindow;

    public LoginPanel(ClientController clientController, MainWindow mainWindow) {
        this.clientController = clientController;
        this.mainWindow = mainWindow;

        setLayout(new GridLayout(3, 2, 10, 10));

        // Username
        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        // Password
        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        // Login Button
        loginButton = new JButton("Login");
        add(loginButton);

        // Register Button
        registerButton = new JButton("Register");
        add(registerButton);

        // Action Listeners
        loginButton.addActionListener(new LoginAction());
        registerButton.addActionListener(new RegisterAction());
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                if (clientController.login(username, password)) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    mainWindow.showPanel("Calendar"); // Switch to CalendarPanel
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(LoginPanel.this, "Error connecting to server.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                clientController.register(username, password);
                JOptionPane.showMessageDialog(LoginPanel.this, "Registration successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(LoginPanel.this, "Error connecting to server.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
