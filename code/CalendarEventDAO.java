package code;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object (DAO) class for managing persistence of calendar events.
 * Provides methods to load and save events to and from file.
 */
public class CalendarEventDAO {

    // File for storing event data
    private static final String EVENTS_FILE = "events.dat";

    /**
     * Loads all calendar events from events file.
     *
     * @return Map where key is the date (LocalDate),
     *              and value is a list of CalendarEvent objects
     */
    public Map<LocalDate, List<CalendarEvent>> loadEvents() {
        File file = new File(EVENTS_FILE);
        if (!file.exists()) {
            return new HashMap<>(); // Return empty map if file doesn't exist
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            // Deserialize, return map of events
            return (Map<LocalDate, List<CalendarEvent>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>(); // Return empty map in case of error
        }
    }

    /**
     * Saves all calendar events to events file.
     *
     * @param events Map where key is the date (LocalDate),
     *               and value is a list of CalendarEvent objects to be saved
     */
    public void saveEvents(Map<LocalDate, List<CalendarEvent>> events) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EVENTS_FILE))) {
            // Serialize, write map of events to file
            oos.writeObject(events);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
