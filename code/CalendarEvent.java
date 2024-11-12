package code;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CalendarEvent class
 * This class will store event details like date, time, and description.
 * These events will be used in the calendar implementation.
 */
public class CalendarEvent implements Serializable {
    private LocalDateTime dateTime;
    private String description;

    // Constructor
    public CalendarEvent(LocalDateTime dateTime, String description) {
        this.dateTime = dateTime;
        this.description = description;
    }

    // Getter: LocalDateTime dateTime
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    // Getter: String description
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Event on " + dateTime + ": " + description;
    }
}