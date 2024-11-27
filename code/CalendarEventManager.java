package code;

import java.time.LocalDate;
import java.util.*;

public class CalendarEventManager {
    private final CalendarEventDAO eventDAO;
    private Map<LocalDate, List<CalendarEvent>> events;

    public CalendarEventManager(CalendarEventDAO eventDAO) {
        this.eventDAO = eventDAO;
        this.events = eventDAO.loadEvents();
    }

    public synchronized void addEvent(LocalDate date, CalendarEvent event) {
        events.putIfAbsent(date, new ArrayList<>());
        events.get(date).add(event);
        eventDAO.saveEvents(events);
    }

    public synchronized List<CalendarEvent> getEventsForDate(LocalDate date) {
        return events.getOrDefault(date, Collections.emptyList());
    }
}
