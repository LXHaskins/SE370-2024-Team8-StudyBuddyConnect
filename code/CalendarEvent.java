package code;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CalendarEvent implements Serializable {
    private String description; // Description of the event
    private String classNumber; // Class number associated with the event
    private LocalDateTime dateTime; // Date and time of the event

    // Constructor
    public CalendarEvent(String description, String classNumber, LocalDateTime dateTime) {
        this.description = description;
        this.classNumber = classNumber;
        this.dateTime = dateTime;
    }

    // Getters
    public String getDescription() {
        return description;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    // toString method to format the output without seconds
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter) + " | " + classNumber + " | " + description;
    }
}
