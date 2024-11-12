package code;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * The Client class establishes a socket connection to the server on a specified IP address and port.
 * It allows the user to send messages to the server from the console.
 * The client stops sending messages when a predefined "stop" string is entered.
 */
public class Client {
    private Socket socket;          // Client socket to connect to server
    private DataOutputStream out;   // Data output stream to send messages to server
    private Scanner in;             // Scanner to read user input from console


    /**
     * Constructor for the Client class.
     * Initializes the client by connecting to the server and setting up output and input streams.
     */
    public Client() {
        try {
            socket = new Socket("127.0.0.1", Server.PORT); // Connect to server on localhost
            out = new DataOutputStream(socket.getOutputStream()); // Setup output stream to send data to server
            in = new Scanner(System.in);                         // Setup input stream to read user input
            writeMessages();                                    // Begin sending messages to server
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to continuously read user input from the console and send it to the server.
     * The client stops sending messages when the stop string ("##") is entered.
     *
     * @throws IOException if there is an issue with input or output operations
     */
    private void writeMessages() throws IOException {
        String line = "";                    // Variable to store each message from user
        while(!line.equals(Server.STOP_STRING)) { // Continue until stop string is entered
            line = in.nextLine();             // Read input from user
            out.writeUTF(line);               // Send input message to server
        }
        close(); // Close connections after finishing
    }

    /**
     * Method to close the socket, output stream, and input scanner.
     *
     * @throws IOException if there is an issue while closing the streams or socket
     */
    private void close() throws IOException {
        socket.close(); // Close socket connection to the server
        out.close();    // Close data output stream
        in.close();     // Close input scanner
    }

    /**
     * Main method to run the client.
     * Creates an instance of the Client, which connects to the server and starts sending messages.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        new Client(); // Instantiate Client object, which sets up and starts client connection
    }
}