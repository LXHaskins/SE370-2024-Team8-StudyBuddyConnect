package code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private Socket socket;
    private DataOutputStream out;

    public Client() {
        createLoginWindow();
    }

    private void createLoginWindow() {
        // Frame for the login window
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 200);
        loginFrame.setLayout(new GridLayout(4, 2));

        // Username and Password fields
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();

        // Login and Register buttons
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                if (sendCredentialsToServer("LOGIN", username, password)) {
                    JOptionPane.showMessageDialog(loginFrame, "Login Successful!");
                    loginFrame.dispose(); // Close the login window
                    createChatWindow();  // Open the chat window
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Login Failed. Try Again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                if (sendCredentialsToServer("REGISTER", username, password)) {
                    JOptionPane.showMessageDialog(loginFrame, "Registration Successful! Please log in.");
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Registration Failed. User may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Adding components to the frame
        loginFrame.add(userLabel);
        loginFrame.add(userField);
        loginFrame.add(passLabel);
        loginFrame.add(passField);
        loginFrame.add(loginButton);
        loginFrame.add(registerButton);

        loginFrame.setVisible(true);
    }

    private boolean sendCredentialsToServer(String command, String username, String password) {
        try {
            socket = new Socket("127.0.0.1", Server.PORT); // Connect to server
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(command + ":" + username + ":" + password); // Send credentials to server

            // Read server response
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String response = in.readUTF();
            socket.close();

            return response.equals(command + "_SUCCESS");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void createChatWindow() {
        JFrame chatFrame = new JFrame("Chat");
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatFrame.setSize(400, 400);
        chatFrame.setLayout(new BorderLayout());

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);

        JTextField messageField = new JTextField();
        JButton sendButton = new JButton("Send");

        sendButton.addActionListener(e -> {
            String message = messageField.getText();
            if (!message.isEmpty()) {
                try {
                    out.writeUTF(message);
                    chatArea.append("Me: " + message + "\n");
                    messageField.setText("");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        chatFrame.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        chatFrame.add(messageField, BorderLayout.SOUTH);
        chatFrame.add(sendButton, BorderLayout.EAST);

        chatFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new Client();
    }
}
