package code;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CalendarPanel extends JPanel {
    private DefaultListModel<String> eventListModel;

    public CalendarPanel() {
        setLayout(new BorderLayout());

        eventListModel = new DefaultListModel<>();
        JList<String> eventList = new JList<>(eventListModel);

        JPanel addEventPanel = new JPanel(new BorderLayout());
        JTextField eventField = new JTextField();
        JButton addEventButton = new JButton("Add Event");

        addEventButton.addActionListener(e -> {
            String event = eventField.getText();
            if (!event.isEmpty()) {
                eventListModel.addElement(event); // Add event to the list
                eventField.setText(""); // Clear the input field
            }
        });

        addEventPanel.add(new JLabel("New Event:"), BorderLayout.WEST);
        addEventPanel.add(eventField, BorderLayout.CENTER);
        addEventPanel.add(addEventButton, BorderLayout.EAST);

        add(new JScrollPane(eventList), BorderLayout.CENTER);
        add(addEventPanel, BorderLayout.SOUTH);
    }
}
