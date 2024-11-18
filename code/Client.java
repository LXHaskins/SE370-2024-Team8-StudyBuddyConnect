package code;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private MainWindow mainWindow;
    private LoginPanel loginPanel;
    private CalendarPanel calendarPanel;

    public Client() {
        // Initialize the main window
        mainWindow = new MainWindow();

        // Initialize the login panel with action listeners
        loginPanel = new LoginPanel(e -> handleLogin(), e -> handleRegister());
        mainWindow.addView(loginPanel, "LOGIN");

        // Initialize the calendar panel
        calendarPanel = new CalendarPanel();
        mainWindow.addView(calendarPanel, "CALENDAR");

        mainWindow.show();
        mainWindow.switchTo("LOGIN"); // Start with the login view
    }

    private void handleLogin() {
        String username = loginPanel.getUsername();
        String password = loginPanel.getPassword();

        if (sendCredentialsToServer("LOGIN", username, password)) {
            JOptionPane.showMessageDialog(null, "Login Successful!");
            mainWindow.switchTo("CALENDAR"); // Switch to the calendar view
        } else {
            JOptionPane.showMessageDialog(null, "Login Failed. Try Again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        String username = loginPanel.getUsername();
        String password = loginPanel.getPassword();

        if (sendCredentialsToServer("REGISTER", username, password)) {
            JOptionPane.showMessageDialog(null, "Registration Successful! Please log in.");
        } else {
            JOptionPane.showMessageDialog(null, "Registration Failed. User may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean sendCredentialsToServer(String command, String username, String password) {
        try {
            Socket socket = new Socket("127.0.0.1", Server.PORT);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(command + ":" + username + ":" + password);

            DataInputStream in = new DataInputStream(socket.getInputStream());
            String response = in.readUTF();
            socket.close();

            return response.equals(command + "_SUCCESS");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
