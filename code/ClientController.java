package code;

import java.io.IOException;

public class ClientController {
    private final Client client;

    public ClientController(Client client) {
        this.client = client;
    }

    // Login functionality
    public boolean login(String username, String password) throws IOException {
        String command = String.format("LOGIN:%s:%s", username, password);
        String response = client.sendRequest(command);
        return response.equals("LOGIN_SUCCESS");
    }

    // Register functionality
    public boolean register(String username, String password) throws IOException {
        String command = String.format("REGISTER:%s:%s", username, password);
        String response = client.sendRequest(command);
        return response.equals("REGISTER_SUCCESS");
    }

    // Event-related functionalities
    public String getEvents(String date) throws IOException {
        return client.sendRequest("GET_EVENTS:" + date);
    }

    public void addEvent(String date, String description, String classNumber, String roomNumber, String dateTime) throws IOException {
        String command = String.format("ADD_EVENT:%s:%s:%s:%s:%s", date, description, classNumber, roomNumber, dateTime);
        String response = client.sendRequest(command);

        if (!response.equals("ADD_EVENT_SUCCESS")) {
            throw new IOException("Failed to add event.");
        }
    }

    public void deleteEvent(String date, String eventDescription) throws IOException {
        String command = String.format("DELETE_EVENT:%s:%s", date, eventDescription);
        String response = client.sendRequest(command);

        if (!response.equals("DELETE_EVENT_SUCCESS")) {
            throw new IOException("Failed to delete event.");
        }
    }

    // Attendee management functionalities
    public void addAttendee(String date, String eventDescription, String username) throws IOException {
        String command = String.format("ADD_ATTENDEE:%s:%s:%s", date, eventDescription, username);
        String response = client.sendRequest(command);

        if (!response.equals("ADD_ATTENDEE_SUCCESS")) {
            throw new IOException("Failed to add attendee.");
        }
    }

    public void removeAttendee(String date, String eventDescription, String username) throws IOException {
        String command = String.format("REMOVE_ATTENDEE:%s:%s:%s", date, eventDescription, username);
        String response = client.sendRequest(command);

        if (!response.equals("REMOVE_ATTENDEE_SUCCESS")) {
            throw new IOException("Failed to remove attendee.");
        }
    }
}
