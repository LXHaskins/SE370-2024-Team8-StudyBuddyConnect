package code;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CredentialDAO {
    private static final String CREDENTIALS_FILE = "credentials.txt";

    public Map<String, String> loadCredentials() {
        Map<String, String> credentials = new HashMap<>();
        File file = new File(CREDENTIALS_FILE);
        if (!file.exists()) {
            return credentials;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                credentials.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return credentials;
    }

    public void saveCredentials(Map<String, String> credentials) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CREDENTIALS_FILE))) {
            for (Map.Entry<String, String> entry : credentials.entrySet()) {
                pw.println(entry.getKey() + ":" + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
