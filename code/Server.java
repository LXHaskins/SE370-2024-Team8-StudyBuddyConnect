package code;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The Server class sets up a server socket that listens
 *      for incoming client connections on a specified port.
 * It receives messages from the client and prints them to the console.
 * The server stops receiving messages when it receives a predefined "stop" string.
 */
public class Server {
    private ServerSocket server;           // Server socket to listen for client connections
    private DataInputStream in;            // Data input stream to receive messages from client
    public static final int PORT = 5150;   // Port number for server to listen on
    public static final String STOP_STRING = "##"; // String that signals the server to stop receiving messages

    /**
     * Constructor for the Server class.
     * Initializes the server on the specified port
     *      and starts accepting client connections.
     */
    public Server() {
        try {
            server = new ServerSocket(PORT); // Bind server to specified port
            iniConnections();                // Start accepting client connections
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to initialize the connections with the client.
     * This method waits for a client to connect, then opens an input stream for receiving messages.
     *
     * @throws IOException if there is an issue with input or output operations
     */
    private void iniConnections() throws IOException {
        Socket clientSocket = server.accept(); // Wait for a client to connect
        in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream())); // Setup input stream
        readMessages();                         // Begin reading messages from client
        close();                                // Close connections once finished
    }

    /**
     * Method to close the input stream and the server socket.
     *
     * @throws IOException if there is an issue while closing the streams or server socket
     */
    private void close() throws IOException {
        in.close();     // Close data input stream
        server.close(); // Close server socket
    }

    /**
     * Method to continuously read messages from the client.
     * Messages are printed to the console. Reading stops when the stop string ("##") is received.
     *
     * @throws IOException if there is an issue with input or output operations
     */
    private void readMessages() throws IOException {
        String line = "";       // Variable to store each incoming message
        while(!line.equals(STOP_STRING)) { // Continue reading until stop string is received
            line = in.readUTF();           // Read a message from client
            System.out.println(line);      // Print the message to console
        }
    }

    /**
     * Main method to run the server.
     * Creates an instance of the Server, which starts listening for client connections.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Instantiate Server object, which sets up and starts server
        new Server();
    }
}