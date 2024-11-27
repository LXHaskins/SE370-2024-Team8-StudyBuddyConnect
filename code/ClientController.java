package code;

import java.io.IOException;

public class ClientController {
    private final Client client;

    public ClientController(Client client) {
        this.client = client;
    }

    public boolean login(String username, String password) throws IOException {
        String response = client.sendRequest("LOGIN:" + username + ":" + password);
        return "LOGIN_SUCCESS".equals(response);
    }

    public void register(String username, String password) throws IOException {
        client.sendRequest("REGISTER:" + username + ":" + password);
    }

    public String getEvents(String date) throws IOException {
        return client.sendRequest("GET_EVENTS:" + date);
    }

    public void addEvent(String eventData) throws IOException {
        client.sendRequest("ADD_EVENT:" + eventData);
    }
}
