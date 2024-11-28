package code;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ServerController {
    private final CalendarEventManager eventManager;
    private final CredentialManager credentialManager;

    public ServerController(CalendarEventDAO eventDAO, CredentialDAO credentialDAO) {
        this.eventManager = new CalendarEventManager(eventDAO);
        this.credentialManager = new CredentialManager(credentialDAO);
    }

    public String handleRequest(String request) {
        String[] parts = request.split(":");
        String command = parts[0];

        switch (command) {
            case "LOGIN":
                return handleLogin(parts);
            case "REGISTER":
                return handleRegister(parts);
            case "GET_EVENTS":
                return handleGetEvents(parts[1]);
            case "ADD_EVENT":
                return handleAddEvent(parts);
            case "DELETE_EVENT":
                return handleDeleteEvent(parts);
            case "ADD_ATTENDEE":
                return handleAddAttendee(parts);
            case "REMOVE_ATTENDEE":
                return handleRemoveAttendee(parts);
            default:
                return "UNKNOWN_COMMAND";
        }
    }

    // Login handling
    private String handleLogin(String[] parts) {
        String username = parts[1];
        String password = parts[2];
        return credentialManager.validateCredentials(username, password) ? "LOGIN_SUCCESS" : "LOGIN_FAILURE";
    }

    // Register handling
    private String handleRegister(String[] parts) {
        String username = parts[1];
        String password = parts[2];
        if (!credentialManager.validateCredentials(username, password)) {
            credentialManager.addCredential(username, password);
            return "REGISTER_SUCCESS";
        }
        return "REGISTER_FAILURE: User already exists.";
    }

    // Event functionalities
    private String handleGetEvents(String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            List<CalendarEvent> events = eventManager.getEventsForDate(localDate);

            if (events.isEmpty()) {
                return "No events scheduled.";
            }

            return events.stream()
                    .map(CalendarEvent::toString)
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            e.printStackTrace();
            return "GET_EVENTS_FAILURE";
        }
    }

    private String handleAddEvent(String[] parts) {
        try {
            LocalDate date = LocalDate.parse(parts[1]);
            String description = parts[2];
            String classNumber = parts[3];
            String roomNumber = parts[4];
            LocalDateTime dateTime = LocalDateTime.parse(parts[5]);

            CalendarEvent event = new CalendarEvent(description, classNumber, roomNumber, dateTime);
            eventManager.addEvent(date, event);
            return "ADD_EVENT_SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "ADD_EVENT_FAILURE";
        }
    }

    private String handleDeleteEvent(String[] parts) {
        try {
            LocalDate date = LocalDate.parse(parts[1]);
            String eventDescription = parts[2];

            List<CalendarEvent> events = eventManager.getEventsForDate(date);
            events.removeIf(event -> event.getDescription().equals(eventDescription));
            eventManager.addEvent(date, null); // Save updated list
            return "DELETE_EVENT_SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "DELETE_EVENT_FAILURE";
        }
    }

    // Attendee functionalities
    private String handleAddAttendee(String[] parts) {
        try {
            LocalDate date = LocalDate.parse(parts[1]);
            String eventDescription = parts[2];
            String username = parts[3];

            List<CalendarEvent> events = eventManager.getEventsForDate(date);
            for (CalendarEvent event : events) {
                if (event.getDescription().equals(eventDescription)) {
                    if (event.addAttendee(username)) {
                        eventManager.addEvent(date, event); // Save updated event
                        return "ADD_ATTENDEE_SUCCESS";
                    } else {
                        return "ADD_ATTENDEE_FAILURE: Attendee already exists.";
                    }
                }
            }
            return "ADD_ATTENDEE_FAILURE: Event not found.";
        } catch (Exception e) {
            e.printStackTrace();
            return "ADD_ATTENDEE_FAILURE";
        }
    }

    private String handleRemoveAttendee(String[] parts) {
        try {
            LocalDate date = LocalDate.parse(parts[1]);
            String eventDescription = parts[2];
            String username = parts[3];

            List<CalendarEvent> events = eventManager.getEventsForDate(date);
            for (CalendarEvent event : events) {
                if (event.getDescription().equals(eventDescription)) {
                    if (event.removeAttendee(username)) {
                        eventManager.addEvent(date, event); // Save updated event
                        return "REMOVE_ATTENDEE_SUCCESS";
                    } else {
                        return "REMOVE_ATTENDEE_FAILURE: Attendee not found.";
                    }
                }
            }
            return "REMOVE_ATTENDEE_FAILURE: Event not found.";
        } catch (Exception e) {
            e.printStackTrace();
            return "REMOVE_ATTENDEE_FAILURE";
        }
    }
}
