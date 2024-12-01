package code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Represents connected client in server-client architecture.
 * Handles communication with client socket and delegates request processing to ServerController.
 */
public class ConnectedClient implements Runnable {

    private final Socket socket; // Socket for communication with client
    private final ServerController serverController; // Controller for handling client requests

    /**
     * Constructs ConnectedClient instance with specified client socket and server controller.
     *
     * @param socket Socket for communication with client
     * @param serverController Controller for handling client requests
     */
    public ConnectedClient(Socket socket, ServerController serverController) {
        this.socket = socket;
        this.serverController = serverController;
    }

    /**
     * Runs client communication loop. Reads requests from client, processes them
     * using ServerController, and sends back responses.
     */
    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String request;
            // Continuously read and process client requests
            while ((request = in.readLine()) != null) {
                String response = serverController.handleRequest(request); // Delegate to server controller
                out.println(response); // Send response back to client
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle and log exceptions
        }
    }
}
