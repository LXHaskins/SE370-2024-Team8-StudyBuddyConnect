package code;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CalendarEvent implements Serializable {
    private String description; // Description of the event
    private String classNumber; // Class number associated with the event
    private String roomNumber;  // Room number for the event
    private LocalDateTime dateTime; // Date and time of the event
    private Set<String> attendees; // List of usernames attending the event

    // Constructor
    public CalendarEvent(String description, String classNumber, String roomNumber, LocalDateTime dateTime) {
        this.description = description;
        this.classNumber = classNumber;
        this.roomNumber = roomNumber;
        this.dateTime = dateTime;
        this.attendees = new HashSet<>(); // Initialize empty attendees list
    }

    // Getters
    public String getDescription() {
        return description;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Set<String> getAttendees() {
        return attendees;
    }

    // Add an attendee
    public boolean addAttendee(String username) {
        return attendees.add(username); // Returns true if added, false if already present
    }

    // Remove an attendee
    public boolean removeAttendee(String username) {
        return attendees.remove(username); // Returns true if removed, false if not present
    }

    // Get a formatted string of all attendees
    public String getFormattedAttendees() {
        if (attendees.isEmpty()) {
            return "No attendees yet.";
        }
        return attendees.stream()
                .sorted() // Sort alphabetically (optional)
                .collect(Collectors.joining("\n"));
    }

    // toString method to format the output without seconds
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter) + " | " + classNumber + " | Room: " + roomNumber + " | " + description
                + " | Attending: " + attendees.size();
    }
}
