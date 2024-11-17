package code;

import javax.swing.JOptionPane;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private LoginWindow loginWindow;
    private CalendarWindow calendarWindow;

    public Client() {
        // Initialize the login window with listeners for login and register actions
        loginWindow = new LoginWindow(e -> handleLogin(), e -> handleRegister());
        loginWindow.show();
    }

    /**
     * Handles the login action from the LoginWindow.
     */
    private void handleLogin() {
        String username = loginWindow.getUsername();
        String password = loginWindow.getPassword();

        if (sendCredentialsToServer("LOGIN", username, password)) {
            loginWindow.close();
            calendarWindow = new CalendarWindow(username); // Create calendar window
            calendarWindow.show(); // Show calendar window
        } else {
            JOptionPane.showMessageDialog(null, "Login Failed. Try Again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles the registration action from the LoginWindow.
     */
    private void handleRegister() {
        String username = loginWindow.getUsername();
        String password = loginWindow.getPassword();

        if (sendCredentialsToServer("REGISTER", username, password)) {
            JOptionPane.showMessageDialog(null, "Registration Successful! Please log in.");
        } else {
            JOptionPane.showMessageDialog(null, "Registration Failed. User may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sends credentials to the server for login or registration.
     */
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
