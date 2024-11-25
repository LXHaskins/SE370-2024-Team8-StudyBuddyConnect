package code;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

public class CalendarPanel extends JPanel {
    private String username; // Logged-in user's username
    private YearMonth currentMonth; // Current year and month being displayed
    private JPanel calendarGrid; // Grid for displaying the calendar
    private JLabel monthLabel; // Label to display the current month and year
    private JLabel userLabel; // Label to display the logged-in user's name
    private CalendarEventManager eventManager; // Manager for user-specific events

    @SuppressWarnings("unused")
    public CalendarPanel() {
        setLayout(new BorderLayout());
        currentMonth = YearMonth.now();
        eventManager = new CalendarEventManager(); // Initialize event manager

        // User display panel
        JPanel userPanel = new JPanel(new BorderLayout());
        userLabel = new JLabel("Welcome, user!", JLabel.CENTER);
        userLabel.setFont(userLabel.getFont().deriveFont(Font.BOLD, 16));
        userPanel.add(userLabel, BorderLayout.NORTH);

        // Header with navigation buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        monthLabel = new JLabel("", JLabel.CENTER);

        prevButton.addActionListener(e -> changeMonth(-1));
        nextButton.addActionListener(e -> changeMonth(1));

        headerPanel.add(prevButton, BorderLayout.WEST);
        headerPanel.add(monthLabel, BorderLayout.CENTER);
        headerPanel.add(nextButton, BorderLayout.EAST);

        userPanel.add(headerPanel, BorderLayout.SOUTH);
        add(userPanel, BorderLayout.NORTH);

        // Calendar grid
        calendarGrid = new JPanel(new GridLayout(0, 7)); // 7 columns for days of the week
        add(calendarGrid, BorderLayout.CENTER);

        updateCalendar();
    }

    public void setUsername(String username) {
        this.username = username;
        userLabel.setText("Welcome, " + username + "!");
        updateCalendar(); // Refresh the calendar to show user-specific data
    }

    @SuppressWarnings("unused")
    private void updateCalendar() {
        calendarGrid.removeAll();

        // Add day headers
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : daysOfWeek) {
            JLabel dayLabel = new JLabel(day, JLabel.CENTER);
            dayLabel.setFont(dayLabel.getFont().deriveFont(Font.BOLD));
            calendarGrid.add(dayLabel);
        }

        LocalDate firstDayOfMonth = currentMonth.atDay(1);
        int daysInMonth = currentMonth.lengthOfMonth();
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue() % 7;

        // Add empty cells for padding
        for (int i = 0; i < firstDayOfWeek; i++) {
            calendarGrid.add(new JLabel());
        }

        // Add buttons for days
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentMonth.atDay(day);
            JButton dayButton = new JButton(String.valueOf(day));

            // Highlight today
            if (date.equals(LocalDate.now())) {
                dayButton.setBackground(Color.CYAN);
            }

            // Highlight days with events
            if (!eventManager.getEventsForDate(date).isEmpty()) {
                dayButton.setBackground(Color.LIGHT_GRAY); // Indicate events
            }

            dayButton.addActionListener(e -> showDayEvents(date));
            calendarGrid.add(dayButton);
        }

        monthLabel.setText(currentMonth.getMonth() + " " + currentMonth.getYear());
        revalidate();
        repaint();
    }

    private void changeMonth(int delta) {
        currentMonth = currentMonth.plusMonths(delta);
        updateCalendar();
    }

    @SuppressWarnings("unused")
    private void showDayEvents(LocalDate date) {
        // Create a dialog window for viewing events and creating new ones
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Events for " + date, true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Display existing events in a list
        List<CalendarEvent> events = eventManager.getEventsForDate(date);
        DefaultListModel<CalendarEvent> eventListModel = new DefaultListModel<>();
        for (CalendarEvent event : events) {
            eventListModel.addElement(event);
        }
        JList<CalendarEvent> eventList = new JList<>(eventListModel);
        eventList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(eventList);

        // Create buttons for managing events
        JButton viewDetailsButton = new JButton("View Details");
        JButton createEventButton = new JButton("Create New Event");

        viewDetailsButton.addActionListener(e -> {
            CalendarEvent selectedEvent = eventList.getSelectedValue();
            if (selectedEvent == null) {
                JOptionPane.showMessageDialog(dialog, "Please select an event first.");
                return;
            }
            manageAttendees(selectedEvent, dialog); // Manage attendees for the selected event
        });

        createEventButton.addActionListener(e -> {
            dialog.dispose(); // Close the current dialog
            createNewEvent(date); // Trigger the event creation process
        });

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(createEventButton);

        // Add components to the dialog
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Show the dialog
        dialog.setVisible(true);
    }

    @SuppressWarnings("unused")
    private void manageAttendees(CalendarEvent event, JDialog parentDialog) {
        // Create a dialog window for managing attendees
        JDialog attendeeDialog = new JDialog(parentDialog, "Manage Attendees", true);
        attendeeDialog.setSize(400, 300);
        attendeeDialog.setLocationRelativeTo(parentDialog);
        attendeeDialog.setLayout(new BorderLayout());

        // Display event details and attendees
        JTextArea eventDetails = new JTextArea(event.toString());
        eventDetails.setEditable(false);

        JTextArea attendeesList = new JTextArea();
        attendeesList.setEditable(false);
        attendeesList.setText(event.getFormattedAttendees()); // Use delegated function

        // Buttons for adding/removing user
        JButton addAttendeeButton = new JButton("Add Me");
        JButton removeAttendeeButton = new JButton("Remove Me");

        addAttendeeButton.addActionListener(e -> {
            if (event.addAttendee(username)) { // Add the logged-in user
                JOptionPane.showMessageDialog(attendeeDialog, "You are now attending this event.");
            } else {
                JOptionPane.showMessageDialog(attendeeDialog, "You are already attending this event.");
            }
            eventDetails.setText(event.toString()); // Update event details
            attendeesList.setText(event.getFormattedAttendees()); // Update attendees list
        });

        removeAttendeeButton.addActionListener(e -> {
            if (event.removeAttendee(username)) { // Remove the logged-in user
                JOptionPane.showMessageDialog(attendeeDialog, "You are no longer attending this event.");
            } else {
                JOptionPane.showMessageDialog(attendeeDialog, "You are not on the attendee list.");
            }
            eventDetails.setText(event.toString()); // Update event details
            attendeesList.setText(event.getFormattedAttendees()); // Update attendees list
        });

        // Panel for attendee management
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addAttendeeButton);
        buttonPanel.add(removeAttendeeButton);

        // Add components to the dialog
        JPanel attendeesPanel = new JPanel(new BorderLayout());
        attendeesPanel.add(new JLabel("Attendees:"), BorderLayout.NORTH);
        attendeesPanel.add(new JScrollPane(attendeesList), BorderLayout.CENTER);

        attendeeDialog.add(new JScrollPane(eventDetails), BorderLayout.NORTH);
        attendeeDialog.add(attendeesPanel, BorderLayout.CENTER);
        attendeeDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Show the dialog
        attendeeDialog.setVisible(true);
    }

    private void createNewEvent(LocalDate date) {
        // Prompt for the hour
        String hourInput = JOptionPane.showInputDialog(this, "Enter hour for the new event (0-23):");
        if (hourInput == null) return; // Cancelled
        int hour = parseTimeInput(hourInput, 0, 23);

        // Prompt for the minute
        String minuteInput = JOptionPane.showInputDialog(this, "Enter minute for the new event (0-59):");
        if (minuteInput == null) return; // Cancelled
        int minute = parseTimeInput(minuteInput, 0, 59);

        // Prompt for the class number
        String classNumber = JOptionPane.showInputDialog(this, "Enter class number for the event:");
        if (classNumber == null || classNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Class number cannot be empty.");
            return;
        }

        // Prompt for the room number
        String roomNumber = JOptionPane.showInputDialog(this, "Enter room number for the event:");
        if (roomNumber == null || roomNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Room number cannot be empty.");
            return;
        }

        // Prompt for the event description
        String newEventDescription = JOptionPane.showInputDialog(this, "Enter description for the event:");
        if (newEventDescription == null || newEventDescription.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Event description cannot be empty.");
            return;
        }

        // Create the event
        LocalDateTime eventTime = LocalDateTime.of(date, LocalDateTime.now().toLocalTime().withHour(hour).withMinute(minute));
        CalendarEvent newEvent = new CalendarEvent(newEventDescription, classNumber, roomNumber, eventTime);

        // Add the event
        eventManager.addEvent(date, newEvent);
        JOptionPane.showMessageDialog(this, "Event added!");
        updateCalendar(); // Refresh calendar to reflect new events
    }

    private int parseTimeInput(String input, int min, int max) {
        try {
            int value = Integer.parseInt(input);
            if (value >= min && value <= max) {
                return value;
            }
        } catch (NumberFormatException e) {
            // Invalid input, fall through to default
        }
        JOptionPane.showMessageDialog(this, "Invalid input. Defaulting to " + min + ".");
        return min; // Default value if input is invalid
    }
}
