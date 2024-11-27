package code;

import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    public MainWindow(ClientController clientController) {
        setTitle("Calendar Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Add LoginPanel and CalendarPanel to the CardLayout
        LoginPanel loginPanel = new LoginPanel(clientController, this);
        CalendarPanel calendarPanel = new CalendarPanel(clientController, "");

        contentPanel.add(loginPanel, "Login");
        contentPanel.add(calendarPanel, "Calendar");

        add(contentPanel);
    }

    public void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }
}
