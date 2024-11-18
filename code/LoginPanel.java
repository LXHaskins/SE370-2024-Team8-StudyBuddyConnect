package code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private JTextField userField;
    private JPasswordField passField;

    public LoginPanel(ActionListener loginListener, ActionListener registerListener) {
        setLayout(new GridLayout(4, 2)); // GridLayout for simplicity

        JLabel userLabel = new JLabel("Username:");
        userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        passField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        // Attach listeners
        loginButton.addActionListener(loginListener);
        registerButton.addActionListener(registerListener);

        add(userLabel);
        add(userField);
        add(passLabel);
        add(passField);
        add(loginButton);
        add(registerButton);
    }

    public String getUsername() {
        return userField.getText();
    }

    public String getPassword() {
        return new String(passField.getPassword());
    }
}
