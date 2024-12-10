package code;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles server-side request processing for Calendar Application.
 * Acts as intermediary between client requests and underlying
 * business logic implemented by CalendarEventManager and CredentialManager.
 */
public class ServerController {

	private final CalendarEventManager eventManager; // Manages calendar events
	private final CredentialManager credentialManager; // Manages user credentials

	/**
	 * Constructs ServerController with specified DAOs.
	 * Initializes event manager and credential manager.
	 *
	 * @param eventDAO DAO for managing event persistence
	 * @param credentialDAO DAO for managing credential persistence
	 */
	public ServerController(CalendarEventDAO eventDAO, CredentialDAO credentialDAO) {
		this.eventManager = new CalendarEventManager(eventDAO);
		this.credentialManager = new CredentialManager(credentialDAO);
	}

	/**
	 * Handles incoming requests from client and delegates them
	 * to appropriate handler based on command.
	 *
	 * @param request Incoming client request
	 * @return Response to client
	 */
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

	/**
	 * Handles login requests by validating provided credentials.
	 *
	 * @param parts Request parts containing username and password
	 * @return "LOGIN_SUCCESS" if credentials are valid,
	 *          otherwise "LOGIN_FAILURE"
	 */
	private String handleLogin(String[] parts) {
		String username = parts[1];
		String password = parts[2];
		return credentialManager.validateCredentials(username, password) ? "LOGIN_SUCCESS" : "LOGIN_FAILURE";
	}

	/**
	 * Handles registration requests by adding new user credentials.
	 *
	 * @param parts Request parts containing username and password
	 * @return "REGISTER_SUCCESS" if registration is successful,
	 *          otherwise "REGISTER_FAILURE"
	 */
	private String handleRegister(String[] parts) {
		String username = parts[1];
		String password = parts[2];
		if (!credentialManager.validateCredentials(username, password)) {
			credentialManager.addCredential(username, password);
			return "REGISTER_SUCCESS";
		}
		return "REGISTER_FAILURE: User already exists.";
	}

	/**
	 * Handles requests to retrieve events for specific date.
	 *
	 * @param date Date for which events are requested
	 * @return List of events as string, or message indicating no events are scheduled
	 */
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

	/**
	 * Handles requests to add new event.
	 *
	 * @param parts Request parts containing event details
	 * @return "ADD_EVENT_SUCCESS" if event is added successfully,
	 *          otherwise "ADD_EVENT_FAILURE"
	 */
	private String handleAddEvent(String[] parts) {
		try {
			LocalDate date = LocalDate.parse(parts[1]);
			String description = parts[2];
			String classNumber = parts[3];
			String roomNumber = parts[4];

			// Parse the dateTime, ensuring a valid format
			String rawDateTime = parts[5];
			if (!rawDateTime.contains(":")) {
				rawDateTime += ":00"; // Add minutes if missing
			}
			LocalDateTime dateTime = LocalDateTime.parse(rawDateTime); // Adjust formatter if necessary

			CalendarEvent event = new CalendarEvent(description, classNumber, roomNumber, dateTime);
			eventManager.addEvent(date, event);
			return "ADD_EVENT_SUCCESS";
		} catch (Exception e) {
			e.printStackTrace();
			return "ADD_EVENT_FAILURE";
		}
	}

	/**
	 * Handles requests to delete event.
	 *
	 * @param parts Request parts containing event details
	 * @return "DELETE_EVENT_SUCCESS" if event is deleted successfully,
	 *          otherwise "DELETE_EVENT_FAILURE"
	 */
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

	/**
	 * Handles requests to add attendee to event.
	 *
	 * @param parts Request parts containing event and attendee details
	 * @return "ADD_ATTENDEE_SUCCESS" if attendee is added successfully,
	 *          otherwise "ADD_ATTENDEE_FAILURE"
	 */
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

	/**
	 * Handles requests to remove attendee from event.
	 *
	 * @param parts Request parts containing event and attendee details
	 * @return "REMOVE_ATTENDEE_SUCCESS" if attendee is removed successfully,
	 *         otherwise "REMOVE_ATTENDEE_FAILURE"
	 */
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
