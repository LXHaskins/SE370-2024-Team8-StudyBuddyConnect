package code;

import java.time.LocalDate;
import java.util.List;

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
                return credentialManager.validateCredentials(parts[1], parts[2]) ? "LOGIN_SUCCESS" : "LOGIN_FAILURE";

            case "REGISTER":
                credentialManager.addCredential(parts[1], parts[2]);
                return "REGISTER_SUCCESS";

            case "GET_EVENTS":
                LocalDate date = LocalDate.parse(parts[1]);
                List<CalendarEvent> events = eventManager.getEventsForDate(date);
                return events.toString();

            case "ADD_EVENT":
                // Add event handling logic (requires parsing event data)
                return "ADD_EVENT_SUCCESS";

            default:
                return "UNKNOWN_COMMAND";
        }
    }
}
