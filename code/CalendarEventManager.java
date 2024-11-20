package code;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalendarEventManager {
    private static final String EVENTS_FILE = "events.dat";
    private HashMap<LocalDate, List<CalendarEvent>> events;

    public CalendarEventManager() {
        events = loadEvents();
    }

    public synchronized void addEvent(LocalDate date, CalendarEvent event) {
        events.putIfAbsent(date, new ArrayList<>());
        events.get(date).add(event);
        saveEvents();
    }

    public synchronized List<CalendarEvent> getEventsForDate(LocalDate date) {
        return events.getOrDefault(date, new ArrayList<>());
    }

    public synchronized void saveEvents() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(EVENTS_FILE))) {
            out.writeObject(events);
        } catch (IOException e) {
            System.out.println("Error saving events: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private HashMap<LocalDate, List<CalendarEvent>> loadEvents() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(EVENTS_FILE))) {
            return (HashMap<LocalDate, List<CalendarEvent>>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No events found or error loading events: " + e.getMessage());
            return new HashMap<>();
        }
    }
}
