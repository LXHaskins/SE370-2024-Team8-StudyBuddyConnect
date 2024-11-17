package code;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

public class LoginWindow {
    private JFrame frame;
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginWindow(ActionListener loginListener, ActionListener registerListener) {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(4, 2));

        JLabel userLabel = new JLabel("Username:");
        userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        passField = new JPasswordField();

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        // Attach listeners provided by the Client class
        loginButton.addActionListener(loginListener);
        registerButton.addActionListener(registerListener);

        frame.add(userLabel);
        frame.add(userField);
        frame.add(passLabel);
        frame.add(passField);
        frame.add(loginButton);
        frame.add(registerButton);
    }

    public String getUsername() {
        return userField.getText();
    }

    public String getPassword() {
        return new String(passField.getPassword());
    }

    public void show() {
        frame.setVisible(true);
    }

    public void close() {
        frame.dispose();
    }
}
