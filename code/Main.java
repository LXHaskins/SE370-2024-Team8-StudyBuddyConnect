package code;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize client connection to the server
            Client client = new Client("localhost", 5150); // Replace "localhost" with your server's IP if needed
            ClientController clientController = new ClientController(client);

            // Start the main window
            SwingUtilities.invokeLater(() -> {
                MainWindow mainWindow = new MainWindow(clientController);
                mainWindow.setVisible(true);
            });

        } catch (IOException e) {
            System.err.println("Unable to connect to the server. Ensure the server is running on port 5150.");
            e.printStackTrace();
        }
    }
}
