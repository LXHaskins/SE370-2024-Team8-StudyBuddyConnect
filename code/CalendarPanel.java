package code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class CalendarPanel extends JPanel {
    private final ClientController clientController;
    private final String username;
    private LocalDate currentDate; // Tracks the currently displayed month
    private final JLabel headerLabel; // Displays the current month and year
    private final JPanel calendarGrid; // Grid for the calendar days

    public CalendarPanel(ClientController clientController, String username) {
        this.clientController = clientController;
        this.username = username;
        this.currentDate = LocalDate.now();

        setLayout(new BorderLayout());

        // Header with navigation
        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        headerLabel = new JLabel("", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));

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

        YearMonth yearMonth = YearMonth.from(currentDate);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = currentDate.withDayOfMonth(1);
        int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday

        // Update header
        headerLabel.setText(yearMonth.getMonth() + " " + yearMonth.getYear());

        // Add day names as the first row
        String[] dayNames = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String dayName : dayNames) {
            JLabel dayLabel = new JLabel(dayName, JLabel.CENTER);
            calendarGrid.add(dayLabel);
        }

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
            showEventsForDate(date);
        }
    }

    private void showEventsForDate(LocalDate date) {
        try {
            String eventsResponse = clientController.getEvents(date.toString());
            List<CalendarEvent> events = parseEvents(eventsResponse);

            StringBuilder eventsText = new StringBuilder("Events for " + date + ":\n");
            for (CalendarEvent event : events) {
                eventsText.append(event.toString()).append("\n");
            }

            if (events.isEmpty()) {
                eventsText.append("No events scheduled.");
            }

            JOptionPane.showMessageDialog(this, eventsText.toString(), "Events", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading events.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<CalendarEvent> parseEvents(String eventsResponse) {
        // Parse the response string from the server into a list of CalendarEvent objects
        // This is a placeholder; implement proper parsing logic
        return List.of(); // Replace with actual parsed events
    }
}
