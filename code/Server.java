package code;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    private ServerSocket server;
    public static final int PORT = 5150;
    private static final String CREDENTIALS_FILE = "credentials.txt";

    private HashMap<String, String> credentials; // Credential storage

    public Server() {
        credentials = new HashMap<>();
        loadCredentials(); // Load credentials from a file

        try {
            server = new ServerSocket(PORT);
            System.out.println("Server started. Waiting for clients...");
            while (true) {
                acceptConnections();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Accepts client connections and delegates communication to ConnectedClient.
     */
    private void acceptConnections() throws IOException {
        Socket clientSocket = server.accept();
        System.out.println("Client connected: " + clientSocket.getInetAddress());

        // Handle the client using ConnectedClient
        new Thread(() -> {
            try {
                ConnectedClient client = new ConnectedClient(clientSocket);

                // Authenticate client
                String loginMessage = client.readInitialMessage(); // Read initial message (LOGIN or REGISTER)
                if (loginMessage.startsWith("LOGIN:")) {
                    handleLogin(client, loginMessage);
                } else if (loginMessage.startsWith("REGISTER:")) {
                    handleRegistration(client, loginMessage);
                } else {
                    client.sendMessage("INVALID_COMMAND");
                }

                client.close(); // Close client connection
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Handles login requests.
     */
    private void handleLogin(ConnectedClient client, String message) throws IOException {
        String[] parts = message.split(":");
        if (parts.length == 3) {
            String username = parts[1];
            String password = parts[2];

            if (authenticate(username, password)) {
                client.sendMessage("LOGIN_SUCCESS");
                System.out.println("User " + username + " logged in successfully.");
            } else {
                client.sendMessage("LOGIN_FAILURE");
                System.out.println("Login failed for user " + username);
            }
        } else {
            client.sendMessage("INVALID_LOGIN_FORMAT");
        }
    }

    /**
     * Handles registration requests.
     */
    private void handleRegistration(ConnectedClient client, String message) throws IOException {
        String[] parts = message.split(":");
        if (parts.length == 3) {
            String username = parts[1];
            String password = parts[2];

            if (credentials.containsKey(username)) {
                client.sendMessage("REGISTRATION_FAILURE:User already exists");
                System.out.println("Registration failed. User " + username + " already exists.");
            } else {
                credentials.put(username, password);
                saveCredentials(); // Save updated credentials to file
                client.sendMessage("REGISTRATION_SUCCESS");
                System.out.println("User " + username + " registered successfully.");
            }
        } else {
            client.sendMessage("INVALID_REGISTRATION_FORMAT");
        }
    }

    /**
     * Authenticates a user by checking the credentials.
     */
    private boolean authenticate(String username, String password) {
        return credentials.containsKey(username) && credentials.get(username).equals(password);
    }

    /**
     * Loads credentials from a file into the credentials map.
     */
    private void loadCredentials() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    credentials.put(parts[0], parts[1]);
                }
            }
            System.out.println("Loaded credentials from file.");
        } catch (FileNotFoundException e) {
            System.out.println("Credentials file not found. Starting with an empty database.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the credentials map to a file.
     */
    private void saveCredentials() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE))) {
            for (String username : credentials.keySet()) {
                writer.write(username + ":" + credentials.get(username));
                writer.newLine();
            }
            System.out.println("Saved credentials to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
