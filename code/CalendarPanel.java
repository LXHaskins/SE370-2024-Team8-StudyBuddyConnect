package code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class CalendarPanel extends JPanel {
    private final ClientController clientController;
    private final String username;
    private LocalDate currentDate; // Tracks the currently displayed month
    private final JLabel headerLabel;
    private final JPanel calendarGrid; // Grid for the calendar days

    public CalendarPanel(ClientController clientController, String username) {
        this.clientController = clientController;
        this.username = username;
        this.currentDate = LocalDate.now();

        setLayout(new BorderLayout());

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(welcomeLabel, BorderLayout.NORTH);

        // Header with month and year
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
        calendarGrid = new JPanel(new GridLayout(0, 7)); // 7 columns for days of the week
        add(calendarGrid, BorderLayout.CENTER);

        // Initialize calendar
        updateCalendar();
    }

    private void navigateMonth(int offset) {
        currentDate = currentDate.plusMonths(offset);
        updateCalendar();
    }

    private void updateCalendar() {
        calendarGrid.removeAll(); // Clear the grid

        // Update header label with the current month and year
        YearMonth yearMonth = YearMonth.from(currentDate);
        headerLabel.setText(yearMonth.getMonth().name() + " " + yearMonth.getYear());

        // Add day buttons
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = currentDate.withDayOfMonth(1);
        int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday

        // Add empty labels for days before the first day of the month
        for (int i = 1; i < firstDayOfWeek; i++) {
            calendarGrid.add(new JLabel(""));
        }

        // Add buttons for each day
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), day);
            JButton dayButton = new JButton(String.valueOf(day));

            // Highlight today's date
            if (date.equals(LocalDate.now())) {
                dayButton.setBackground(Color.CYAN);
            }

            dayButton.addActionListener(new DayButtonAction(date));
            calendarGrid.add(dayButton);
        }

        // Refresh the calendar
        revalidate();
        repaint();
    }

    private class DayButtonAction implements ActionListener {
        private final LocalDate date;

        public DayButtonAction(LocalDate date) {
            this.date = date;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Display events and options for the selected day
            showDayDetails(date);
        }
    }

    private void showDayDetails(LocalDate date) {
        try {
            // Fetch events for the selected day
            String eventsResponse = clientController.getEvents(date.toString());
            List<String> events = parseEvents(eventsResponse);

            JPanel detailsPanel = new JPanel(new BorderLayout());

            // Display the list of events
            JTextArea eventsTextArea = new JTextArea();
            eventsTextArea.setEditable(false);
            if (events.isEmpty()) {
                eventsTextArea.setText("No events for this day.");
            } else {
                events.forEach(event -> eventsTextArea.append(event + "\n"));
            }

            detailsPanel.add(new JScrollPane(eventsTextArea), BorderLayout.CENTER);

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
                String eventToRemove = JOptionPane.showInputDialog(this, "Enter event description to remove:");
                if (eventToRemove != null && !eventToRemove.trim().isEmpty()) {
                    try {
                        clientController.deleteEvent(date.toString(), eventToRemove);
                        JOptionPane.showMessageDialog(this, "Event removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Failed to remove event. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            // Buttons Panel
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(addEventButton);
            buttonsPanel.add(removeEventButton);

            detailsPanel.add(buttonsPanel, BorderLayout.SOUTH);

            // Show the details dialog
            JOptionPane.showMessageDialog(this, detailsPanel, "Events for " + date, JOptionPane.PLAIN_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading events.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<String> parseEvents(String eventsResponse) {
        // Parse the server response into a list of event strings
        String[] lines = eventsResponse.split("\n");
        List<String> events = new ArrayList<>();
        for (String line : lines) {
            events.add(line.trim());
        }
        return events;
    }
}
