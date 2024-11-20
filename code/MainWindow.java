package code;

import javax.swing.*;
import java.awt.*;

public class MainWindow {
    private JFrame frame;
    private JPanel mainPanel; // Main container with CardLayout
    private CardLayout cardLayout;
    private CalendarPanel calendarPanel; // Reference to CalendarPanel

    public MainWindow(Client client) {
        frame = new JFrame("Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null); // Center on screen

        // Set up CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add LoginPanel
        LoginPanel loginPanel = new LoginPanel(client, this); // Pass reference to MainWindow
        mainPanel.add(loginPanel, "LOGIN");

        // Initialize and Add CalendarPanel
        calendarPanel = new CalendarPanel(); // Use your existing CalendarPanel class
        mainPanel.add(calendarPanel, "CALENDAR");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public void switchToCalendar(String username) {
        calendarPanel.setUsername(username); // If your CalendarPanel needs the username
        cardLayout.show(mainPanel, "CALENDAR");
    }

    public void show() {
        frame.setVisible(true);
    }
}
