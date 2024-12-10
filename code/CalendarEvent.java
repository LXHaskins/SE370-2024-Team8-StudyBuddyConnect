package code;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents calendar event with details such as description,
 * class number, room number, date and time, and list of attendees.
 */
public class CalendarEvent implements Serializable {

    private String description; // Description of event
    private String classNumber; // Associated class number
    private String roomNumber; // Room where event takes place
    private LocalDateTime dateTime; // Date and time of event
    private Set<String> attendees; // Attendees for event

    /**
     * Constructs new CalendarEvent object with specified details.
     *
     * @param description Description of event
     * @param classNumber Class number associated with event
     * @param roomNumber Room where event will occur
     * @param dateTime Date and time of event
     */
    public CalendarEvent(String description, String classNumber, String roomNumber, LocalDateTime dateTime) {
        this.description = description;
        this.classNumber = classNumber;
        this.roomNumber = roomNumber;
        this.dateTime = dateTime;
        this.attendees = new HashSet<>(); // Initialize attendees as empty set
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Set<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(Set<String> attendees) {
        this.attendees = attendees;
    }

    /**
     * Adds attendee to event.
     *
     * @param username Username of attendee to be added
     * @return True if attendee was added, false if already present
     */
    public boolean addAttendee(String username) {
        return attendees.add(username); // Adds attendee if not already present
    }

    /**
     * Removes attendee from event.
     *
     * @param username Username of attendee to be removed
     * @return True if attendee was removed, false if not present
     */
    public boolean removeAttendee(String username) {
        return attendees.remove(username); // Removes attendee if present
    }

    @Override
    public String toString() {
        return String.format("%s | %s | Room: %s | Time: %s | Attendees: %s",
                description, classNumber, roomNumber, dateTime.toLocalTime(), attendees);
    }
}
