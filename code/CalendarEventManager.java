package code;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Manages calendar events by interacting with data access object (DAO) and
 * providing functionality to add and retrieve events.
 */
public class CalendarEventManager {

    private final CalendarEventDAO eventDAO; // DAO for event persistence
    private Map<LocalDate, List<CalendarEvent>> events; // Map of events by date

    /**
     * Constructs CalendarEventManager with specified DAO for event persistence.
     *
     * @param eventDAO Data access object responsible for loading and saving events
     */
    public CalendarEventManager(CalendarEventDAO eventDAO) {
        this.eventDAO = eventDAO;
        this.events = eventDAO.loadEvents(); // Load events from the DAO
    }

    /**
     * Adds new event for specified date.
     * If no events exist for date, a new list is created.
     * Updated event data is then saved using DAO.
     *
     * @param date  Date for which event is being added
     * @param event Event to be added
     */
    public synchronized void addEvent(LocalDate date, CalendarEvent event) {
        events.putIfAbsent(date, new ArrayList<>()); // Initialize list if absent
        events.get(date).add(event); // Add event to list for date
        eventDAO.saveEvents(events); // Save updated map of events
    }

    /**
     * Retrieves a list of events for specified date.
     * If no events exist for date, an empty list is returned.
     *
     * @param date Date for which events are being retrieved
     * @return List of events for specified date, or an empty list if none exist
     */
    public synchronized List<CalendarEvent> getEventsForDate(LocalDate date) {
        return events.getOrDefault(date, Collections.emptyList()); // Return events or empty list
    }
}
