package code;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 5150;
    private CredentialManager credentialManager;

    public Server() {
        credentialManager = new CredentialManager();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for clients...");
            acceptConnections(serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptConnections(ServerSocket serverSocket) {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                new Thread(() -> handleClient(clientSocket)).start();
            } catch (IOException e) {
                System.out.println("Error accepting client connection: " + e.getMessage());
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (DataInputStream in = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {

            String request = in.readUTF();
            String[] parts = request.split(":", 3); // Command:username:password

            if (parts.length < 3) {
                out.writeUTF("INVALID_REQUEST");
                return;
            }

            String command = parts[0];
            String username = parts[1];
            String password = parts[2];

            switch (command) {
                case "LOGIN":
                    handleLogin(out, username, password);
                    break;
                case "REGISTER":
                    handleRegister(out, username, password);
                    break;
                default:
                    out.writeUTF("UNKNOWN_COMMAND");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLogin(DataOutputStream out, String username, String password) throws IOException {
        if (credentialManager.authenticate(username, password)) {
            out.writeUTF("LOGIN_SUCCESS");
        } else {
            out.writeUTF("LOGIN_FAILURE");
        }
    }

    private void handleRegister(DataOutputStream out, String username, String password) throws IOException {
        if (credentialManager.register(username, password)) {
            out.writeUTF("REGISTRATION_SUCCESS");
        } else {
            out.writeUTF("REGISTRATION_FAILURE");
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
