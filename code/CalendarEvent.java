package code;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class CalendarEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String description;
    private String classNumber;
    private String roomNumber;
    private LocalDateTime dateTime;
    private Set<String> attendees; // A set to store unique attendees

    // Getters and setters for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getters and setters for class number
    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    // Getters and setters for room number
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    // Getters and setters for date/time
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    // Getters and setters for attendees
    public Set<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(Set<String> attendees) {
        this.attendees = attendees;
    }

    // Add attendee
    public boolean addAttendee(String username) {
        return attendees.add(username); // Add attendee if not already present
    }

    // Remove attendee
    public boolean removeAttendee(String username) {
        return attendees.remove(username); // Remove attendee if present
    }

    public CalendarEvent(String description, String classNumber, String roomNumber, LocalDateTime dateTime) {
        this.description = description;
        this.classNumber = classNumber;
        this.roomNumber = roomNumber;
        this.dateTime = dateTime;
        this.attendees = new HashSet<>(); // Initialize attendees
    }

    @Override
    public String toString() {
        return String.format("%s | %s | Room: %s | Time: %s | Attendees: %s",
                description, classNumber, roomNumber, dateTime.toLocalTime(), attendees);
    }
}
