package code;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * Swing panel that displays calendar interface, allowing user
 * to navigate months, view events for specific days, and manage events.
 */
public class CalendarPanel extends JPanel {

    private final ClientController clientController; // Controller for client-server communication
    private final String username; // Username of logged-in user
    private LocalDate currentDate; // Currently displayed month
    private final JLabel headerLabel; // Displays current month and year
    private final JPanel calendarGrid; // Grid for displaying days in calendar

    /**
     * Constructs a CalendarPanel object with specified client controller and username.
     *
     * @param clientController Controller for handling client-server interactions
     * @param username Username of logged-in user
     */
    public CalendarPanel(ClientController clientController, String username) {
        this.clientController = clientController;
        this.username = username;
        this.currentDate = LocalDate.now();

        setLayout(new BorderLayout());

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(welcomeLabel, BorderLayout.NORTH);

        // Header with month and year navigation
        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        headerLabel = new JLabel("", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));

        prevButton.addActionListener(e -> navigateMonth(-1));
        nextButton.addActionListener(e -> navigateMonth(1));

        headerPanel.add(prevButton, BorderLayout.WEST);
        headerPanel.add(nextButton, BorderLayout.EAST);
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Calendar grid
        calendarGrid = new JPanel(new GridLayout(0, 7)); // 7 columns for days of week
        add(calendarGrid, BorderLayout.CENTER);

        // Initialize calendar
        updateCalendar();
    }

    /**
     * Navigates calendar by specified number of months and updates view.
     *
     * @param offset Number of months to move forward (positive) or backward (negative)
     */
    private void navigateMonth(int offset) {
        currentDate = currentDate.plusMonths(offset);
        updateCalendar();
    }

    /**
     * Updates calendar grid to reflect currently displayed month and year.
     */
    private void updateCalendar() {
        calendarGrid.removeAll(); // Clear grid

        // Update header label with current month and year
        YearMonth yearMonth = YearMonth.from(currentDate);
        headerLabel.setText(yearMonth.getMonth().name() + " " + yearMonth.getYear());

        // Add empty labels for days before first day of month
        LocalDate firstOfMonth = currentDate.withDayOfMonth(1);
        int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday
        for (int i = 1; i < firstDayOfWeek; i++) {
            calendarGrid.add(new JLabel(""));
        }

        // Add buttons for each day of month
        int daysInMonth = yearMonth.lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), day);
            JButton dayButton = new JButton(String.valueOf(day));

            // Highlight for today's date
            if (date.equals(LocalDate.now())) {
                dayButton.setBackground(Color.CYAN);
            }

            dayButton.addActionListener(new DayButtonAction(date));
            calendarGrid.add(dayButton);
        }

        // Refresh calendar display
        revalidate();
        repaint();
    }

    /**
     * Handles actions for selecting specific day in calendar.
     */
    private class DayButtonAction implements ActionListener {
        private final LocalDate date;

        public DayButtonAction(LocalDate date) {
            this.date = date;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Display events and options for selected day
            showDayDetails(date);
        }
    }

    /**
     * Displays details of events for a selected day, allowing user
     * to add or remove events.
     *
     * @param date Selected date
     */
    private void showDayDetails(LocalDate date) {
        try {
            // Fetch events for the selected day
            String eventsResponse = clientController.getEvents(date.toString());
            List<String> events = parseEvents(eventsResponse);

            // Initialize UI components
            JPanel detailsPanel = new JPanel(new BorderLayout());
            DefaultListModel<String> listModel = new DefaultListModel<>();
            events.forEach(listModel::addElement);
            JList<String> eventList = new JList<>(listModel);
            eventList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            detailsPanel.add(new JScrollPane(eventList), BorderLayout.CENTER);

            // Add Event Button
            JButton addEventButton = new JButton("Add Event");
            addEventButton.addActionListener(e -> {
                String description = JOptionPane.showInputDialog(this, "Enter event description for " + date + ":");
                if (description != null && !description.trim().isEmpty()) {
                    try {
                        String timeInput = JOptionPane.showInputDialog(this, "Enter event time (HH:mm):");
                        String classNumber = JOptionPane.showInputDialog(this, "Enter class number:");
                        String roomNumber = JOptionPane.showInputDialog(this, "Enter room number:");
                        String dateTime = date.toString() + "T" + timeInput;

                        clientController.addEvent(date.toString(), description, classNumber, roomNumber, dateTime);
                        listModel.addElement(description); // Update the event list
                        JOptionPane.showMessageDialog(this, "Event added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Failed to add event. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            // Remove Event Button
            JButton removeEventButton = new JButton("Remove Event");
            removeEventButton.addActionListener(e -> {
                String selectedEvent = eventList.getSelectedValue();
                if (selectedEvent != null) {
                    try {
                        clientController.deleteEvent(date.toString(), selectedEvent);
                        listModel.removeElement(selectedEvent); // Update the event list
                        JOptionPane.showMessageDialog(this, "Event removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Failed to remove event. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No event selected.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // View Event Details Button
            JButton viewDetailsButton = new JButton("View Details");
            viewDetailsButton.addActionListener(e -> {
                String selectedEvent = eventList.getSelectedValue();
                if (selectedEvent != null) {
                    showEventDetails(date, selectedEvent); // Open detailed menu for the selected event
                } else {
                    JOptionPane.showMessageDialog(this, "No event selected.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Buttons Panel
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(addEventButton);
            buttonsPanel.add(removeEventButton);
            buttonsPanel.add(viewDetailsButton);

            detailsPanel.add(buttonsPanel, BorderLayout.SOUTH);

            // Show details dialog
            JOptionPane.showMessageDialog(this, detailsPanel, "Events for " + date, JOptionPane.PLAIN_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading events.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showEventDetails(LocalDate date, String eventDescription) {
        try {
            // Fetch attendees for the selected event
            String attendeesResponse = clientController.getAttendees(date.toString(), eventDescription);

            // Handle null or empty response gracefully
            if (attendeesResponse == null || attendeesResponse.trim().isEmpty()) {
                attendeesResponse = "No attendees available.";
            }

            List<String> attendees = parseEvents(attendeesResponse);

            JPanel detailsPanel = new JPanel(new BorderLayout());

            // Display event description and attendees
            JTextArea attendeesTextArea = new JTextArea();
            attendeesTextArea.setEditable(false);
            attendeesTextArea.setText("Event: " + eventDescription + "\n\nAttendees:\n");
            if (attendees.isEmpty() || attendeesResponse.equals("No attendees available.")) {
                attendeesTextArea.append("No attendees yet.");
            } else {
                attendees.forEach(attendee -> attendeesTextArea.append(attendee + "\n"));
            }

            detailsPanel.add(new JScrollPane(attendeesTextArea), BorderLayout.CENTER);

            // Add and Remove Attendee Buttons
            JButton addSelfButton = new JButton("Join Event");
            addSelfButton.addActionListener(e -> {
                try {
                    clientController.addAttendee(date.toString(), eventDescription, username);
                    attendees.add(username);
                    attendeesTextArea.append(username + "\n");
                    JOptionPane.showMessageDialog(this, "You have joined the event!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Failed to join event. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            JButton removeSelfButton = new JButton("Leave Event");
            removeSelfButton.addActionListener(e -> {
                if (attendees.contains(username)) {
                    try {
                        clientController.removeAttendee(date.toString(), eventDescription, username);
                        attendees.remove(username);
                        attendeesTextArea.setText("Event: " + eventDescription + "\n\nAttendees:\n");
                        if (attendees.isEmpty()) {
                            attendeesTextArea.append("No attendees yet.");
                        } else {
                            attendees.forEach(attendee -> attendeesTextArea.append(attendee + "\n"));
                        }
                        JOptionPane.showMessageDialog(this, "You have left the event.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Failed to leave event. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "You are not an attendee of this event.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Buttons Panel
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(addSelfButton);
            buttonsPanel.add(removeSelfButton);

            detailsPanel.add(buttonsPanel, BorderLayout.SOUTH);

            // Show event details dialog
            JOptionPane.showMessageDialog(this, detailsPanel, "Details for: " + eventDescription, JOptionPane.PLAIN_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading event details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Parses server response containing event data into list of event descriptions.
     *
     * @param eventsResponse Server response containing event data
     * @return List of event descriptions
     */
    private List<String> parseEvents(String eventsResponse) {
        String[] lines = eventsResponse.split("\n");
        List<String> events = new ArrayList<>();
        for (String line : lines) {
            events.add(line.trim());
        }
        return events;
    }
}