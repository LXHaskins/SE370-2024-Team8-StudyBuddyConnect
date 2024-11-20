package code;

import java.io.*;
import java.util.HashMap;

public class CredentialManager {
    private static final String CREDENTIALS_FILE = "credentials.txt";
    private HashMap<String, String> credentials;

    public CredentialManager() {
        credentials = new HashMap<>();
        loadCredentials();
    }

    public synchronized boolean authenticate(String username, String password) {
        return credentials.containsKey(username) && credentials.get(username).equals(password);
    }

    public synchronized boolean register(String username, String password) {
        if (credentials.containsKey(username)) {
            return false; // Username already exists
        }
        credentials.put(username, password);
        saveCredentials();
        return true;
    }

    private void loadCredentials() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    credentials.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load credentials: " + e.getMessage());
        }
    }

    private void saveCredentials() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE))) {
            for (String username : credentials.keySet()) {
                writer.write(username + ":" + credentials.get(username));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save credentials: " + e.getMessage());
        }
    }
}
