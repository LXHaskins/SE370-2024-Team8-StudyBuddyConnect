/**
 * CalendarEvent class
 *
 * This class will store event details like date, time, and description.
 * These events will be used in the calendar implementation.
 */

import java.io.Serializable;
import java.time.LocalDateTime;

public class CalendarEvent implements Serializable {
    private LocalDateTime dateTime;
    private String description;

    public CalendarEvent(LocalDateTime dateTime, String description) {
        this.dateTime = dateTime;
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Event on " + dateTime + ": " + description;
    }
}