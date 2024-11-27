package code;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class CalendarEventDAO {
    private static final String EVENTS_FILE = "events.dat";

    public Map<LocalDate, List<CalendarEvent>> loadEvents() {
        File file = new File(EVENTS_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<LocalDate, List<CalendarEvent>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public void saveEvents(Map<LocalDate, List<CalendarEvent>> events) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EVENTS_FILE))) {
            oos.writeObject(events);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
