package code;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CalendarEvent implements Serializable {
    private String description;
    private String classNumber; // Associated class
    private LocalDateTime dateTime; // Date and time of the event

    public CalendarEvent(String description, String classNumber, LocalDateTime dateTime) {
        this.description = description;
        this.classNumber = classNumber;
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return dateTime + " | " + classNumber + " | " + description;
    }
}
