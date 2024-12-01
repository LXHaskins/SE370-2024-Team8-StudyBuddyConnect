package code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Access Object (DAO) class for managing user credentials.
 * Provides functionality to load and save credentials from/to file.
 */
public class CredentialDAO {

    private static final String CREDENTIALS_FILE = "credentials.txt"; // File to store credentials

    /**
     * Loads user credentials from credentials file.
     *
     * @return Map where key is the username and value is the corresponding password
     */
    public Map<String, String> loadCredentials() {
        Map<String, String> credentials = new HashMap<>();
        File file = new File(CREDENTIALS_FILE);

        // If file does not exist, return an empty map
        if (!file.exists()) {
            return credentials;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            // Read each line from file and parse it into username and password
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                credentials.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Log exceptions
        }

        return credentials;
    }

    /**
     * Saves user credentials to credentials file.
     *
     * @param credentials Map where key is the username and value is the corresponding password
     */
    public void saveCredentials(Map<String, String> credentials) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CREDENTIALS_FILE))) {
            // Write each username-password pair to file (format: username:password)
            for (Map.Entry<String, String> entry : credentials.entrySet()) {
                pw.println(entry.getKey() + ":" + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace(); // Log exceptions
        }
    }
}
