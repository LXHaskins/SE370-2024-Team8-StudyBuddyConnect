package code;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

/**
 * Represents main application window for Calendar Application.
 * Uses CardLayout to switch between different panels,
 * such as LoginPanel and CalendarPanel.
 */
public class MainWindow extends JFrame {

    private final CardLayout cardLayout; // Layout manager for switching between panels
    private final JPanel contentPanel; // Panel container for CardLayout

    /**
     * Constructs MainWindow with specified client controller.
     * Initializes application window, adds LoginPanel and CalendarPanel,
     * and sets up CardLayout for panel switching.
     *
     * @param clientController Controller for managing client-server communication
     */
    public MainWindow(ClientController clientController) {
        // Set up main window properties
        setTitle("Calendar Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Initialize CardLayout and content panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Create and add panels to CardLayout
        LoginPanel loginPanel = new LoginPanel(clientController, this);
        CalendarPanel calendarPanel = new CalendarPanel(clientController, "");

        contentPanel.add(loginPanel, "Login");
        contentPanel.add(calendarPanel, "Calendar");

        // Add content panel to main window
        add(contentPanel);
    }

    /**
     * Displays specified panel by name using CardLayout.
     *
     * @param panelName Name of panel to display (ex: "Login", "Calendar")
     */
    public void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }
}