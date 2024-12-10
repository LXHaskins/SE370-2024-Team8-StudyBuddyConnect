package code;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Represents server component of Calendar Application.
 * Listens for client connections, handles requests, and delegates
 * request processing to ServerController.
 */
public class Server {

    private static final int PORT = 5150; // Port number for server

    // Thread pool for handling client connections
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    /**
     * Starts server, initializes DAOs and ServerController, and accepts client connections.
     */
    public void start() {
        System.out.println("Server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            // Initialize DAOs and server controller
            CalendarEventDAO eventDAO = new CalendarEventDAO();
            CredentialDAO credentialDAO = new CredentialDAO();
            ServerController serverController = new ServerController(eventDAO, credentialDAO);

            // Continuously accept client connections
            while (true) {
                Socket clientSocket = serverSocket.accept(); // Wait for client connection

                // Handle client in a new thread
                threadPool.submit(new ConnectedClient(clientSocket, serverController));
            }
        } catch (IOException e) {
            e.printStackTrace(); // Log any I/O exceptions
        }
    }

    /**
     * Entry point for server application. Creates and starts new server instance.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        new Server().start();
    }
}
