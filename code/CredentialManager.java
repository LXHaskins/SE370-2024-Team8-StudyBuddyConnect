package code;

import java.util.Map;

public class CredentialManager {
    private final CredentialDAO credentialDAO;
    private final Map<String, String> credentials;

    public CredentialManager(CredentialDAO credentialDAO) {
        this.credentialDAO = credentialDAO;
        this.credentials = credentialDAO.loadCredentials();
    }

    public boolean validateCredentials(String username, String password) {
        return credentials.containsKey(username) && credentials.get(username).equals(password);
    }

    public void addCredential(String username, String password) {
        credentials.put(username, password);
        credentialDAO.saveCredentials(credentials);
    }
}
