package code;

import javax.swing.*;
import java.awt.*;

public class MainWindow {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public MainWindow() {
        frame = new JFrame("Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        frame.add(mainPanel);

        // Center the window on the screen
        frame.setLocationRelativeTo(null);
    }

    public void addView(JPanel panel, String name) {
        mainPanel.add(panel, name);
    }

    public void switchTo(String viewName) {
        cardLayout.show(mainPanel, viewName);
    }

    public void show() {
        frame.setVisible(true);
    }
}
