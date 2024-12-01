package code;

import java.io.IOException;

/**
 * Controller for handling client-side operations and managing communication
 * with server through Client class. Provides methods for user authentication,
 * event management, and attendee management.
 */
public class ClientController {

    private final Client client; // Client for sending requests to server

    /**
     * Constructs ClientController with specified client.
     *
     * @param client Client used for communication with server
     */
    public ClientController(Client client) {
        this.client = client;
    }

    /**
     * Attempts to log in user with specified username and password.
     *
     * @param username Username of the user
     * @param password Password of the user
     * @return True if login was successful, false otherwise
     * @throws IOException - If an error occurs during server communication
     */
    public boolean login(String username, String password) throws IOException {
        String command = String.format("LOGIN:%s:%s", username, password);
        String response = client.sendRequest(command);
        return response.equals("LOGIN_SUCCESS");
    }

    /**
     * Registers new user with specified username and password.
     *
     * @param username Username for new user
     * @param password Password for new user
     * @return True if registration was successful, false otherwise
     * @throws IOException - If an error occurs during server communication
     */
    public boolean register(String username, String password) throws IOException {
        String command = String.format("REGISTER:%s:%s", username, password);
        String response = client.sendRequest(command);
        return response.equals("REGISTER_SUCCESS");
    }

    /**
     * Retrieves events for specified date.
     *
     * @param date Date for which events are to be retrieved (format: YYYY-MM-DD)
     * @return String containing list of events for specified date
     * @throws IOException - If an error occurs during server communication
     */
    public String getEvents(String date) throws IOException {
        return client.sendRequest("GET_EVENTS:" + date);
    }

    /**
     * Adds new event for specified date.
     *
     * @param date        Date of event (format: YYYY-MM-DD)
     * @param description Brief description of event
     * @param classNumber Class number associated with event
     * @param roomNumber  Room where event takes place
     * @param dateTime    Date and time of event (format: YYYY-MM-DDTHH:mm)
     * @throws IOException - If an error occurs during server communication or operation fails
     */
    public void addEvent(String date, String description, String classNumber, String roomNumber, String dateTime) throws IOException {
        String command = String.format("ADD_EVENT:%s:%s:%s:%s:%s", date, description, classNumber, roomNumber, dateTime);
        String response = client.sendRequest(command);

        if (!response.equals("ADD_EVENT_SUCCESS")) {
            throw new IOException("Failed to add event.");
        }
    }

    /**
     * Deletes event for specified date and description.
     *
     * @param date            Date of event (format: YYYY-MM-DD)
     * @param eventDescription Description of event to be deleted
     * @throws IOException - If an error occurs during server communication or operation fails
     */
    public void deleteEvent(String date, String eventDescription) throws IOException {
        String command = String.format("DELETE_EVENT:%s:%s", date, eventDescription);
        String response = client.sendRequest(command);

        if (!response.equals("DELETE_EVENT_SUCCESS")) {
            throw new IOException("Failed to delete event.");
        }
    }

    /**
     * Adds attendee to event for specified date and description.
     *
     * @param date            Date of event (format: YYYY-MM-DD)
     * @param eventDescription Description of event
     * @param username         Username of attendee to be added
     * @throws IOException - If an error occurs during server communication or operation fails
     */
    public void addAttendee(String date, String eventDescription, String username) throws IOException {
        String command = String.format("ADD_ATTENDEE:%s:%s:%s", date, eventDescription, username);
        String response = client.sendRequest(command);

        if (!response.equals("ADD_ATTENDEE_SUCCESS")) {
            throw new IOException("Failed to add attendee.");
        }
    }

    /**
     * Removes attendee from event for specified date and description.
     *
     * @param date            Date of event (format: YYYY-MM-DD)
     * @param eventDescription Description of event
     * @param username         Username of attendee to be removed
     * @throws IOException - If an error occurs during server communication or operation fails
     */
    public void removeAttendee(String date, String eventDescription, String username) throws IOException {
        String command = String.format("REMOVE_ATTENDEE:%s:%s:%s", date, eventDescription, username);
        String response = client.sendRequest(command);

        if (!response.equals("REMOVE_ATTENDEE_SUCCESS")) {
            throw new IOException("Failed to remove attendee.");
        }
    }
}
