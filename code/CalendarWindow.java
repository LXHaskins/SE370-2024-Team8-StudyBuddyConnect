package code;

import javax.swing.*;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

public class CalendarWindow {
    private JFrame frame;
    private List<String> events;
    private DefaultListModel<String> eventListModel;

    public CalendarWindow(String username) {
        frame = new JFrame("Calendar - Welcome, " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        events = new ArrayList<>();
        eventListModel = new DefaultListModel<>();
        JList<String> eventList = new JList<>(eventListModel);

        JPanel addEventPanel = new JPanel(new BorderLayout());
        JTextField eventField = new JTextField();
        JButton addEventButton = new JButton("Add Event");

        addEventButton.addActionListener(e -> {
            String event = eventField.getText();
            if (!event.isEmpty()) {
                events.add(event);
                eventListModel.addElement(event); // Add event to UI list
                eventField.setText(""); // Clear the input field
            }
        });

        addEventPanel.add(new JLabel("New Event:"), BorderLayout.WEST);
        addEventPanel.add(eventField, BorderLayout.CENTER);
        addEventPanel.add(addEventButton, BorderLayout.EAST);

        frame.add(addEventPanel, BorderLayout.SOUTH);
        frame.add(new JScrollPane(eventList), BorderLayout.CENTER);
    }

    public void show() {
        frame.setVisible(true);
    }
}
