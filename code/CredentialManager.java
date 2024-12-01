package code;

import java.util.Map;

/**
 * Manages user credentials by interacting with CredentialDAO for data persistence.
 * Provides functionality for validating and adding credentials.
 */
public class CredentialManager {

    private final CredentialDAO credentialDAO; // DAO for loading and saving credentials
    private final Map<String, String> credentials; // In-memory storage for credentials

    /**
     * Constructs CredentialManager with specified CredentialDAO.
     * Loads credentials from DAO during initialization.
     *
     * @param credentialDAO DAO used for persisting and retrieving credentials
     */
    public CredentialManager(CredentialDAO credentialDAO) {
        this.credentialDAO = credentialDAO;
        this.credentials = credentialDAO.loadCredentials();
    }

    /**
     * Validates provided username and password against stored credentials.
     *
     * @param username Username to validate
     * @param password Password to validate
     * @return True if username exists and password matches, false otherwise
     */
    public boolean validateCredentials(String username, String password) {
        return credentials.containsKey(username) && credentials.get(username).equals(password);
    }

    /**
     * Adds new username-password pair to credentials and saves updated credentials.
     *
     * @param username Username to add
     * @param password Password to associate with username
     */
    public void addCredential(String username, String password) {
        credentials.put(username, password);
        credentialDAO.saveCredentials(credentials); // Persist updated credentials
    }
}
