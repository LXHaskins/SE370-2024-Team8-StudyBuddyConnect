package code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Client client;
    private MainWindow mainWindow;

    public LoginPanel(Client client, MainWindow mainWindow) {
        this.client = client;
        this.mainWindow = mainWindow;

        setLayout(new GridLayout(3, 2));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this::handleLogin);
        add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(this::handleRegister);
        add(registerButton);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (client.login(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            mainWindow.switchToCalendar(username); // Pass username to MainWindow
        } else {
            JOptionPane.showMessageDialog(this, "Login failed. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (client.register(username, password)) {
            JOptionPane.showMessageDialog(this, "Registration successful!");
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Username may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
