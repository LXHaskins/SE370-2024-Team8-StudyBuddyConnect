package code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Represents client-side communication layer for interacting with server.
 * Manages socket connections and enables sending and receiving data.
 */
public class Client {

    private final Socket socket; // Socket connection to server
    private final BufferedReader in; // Input stream for reading server responses
    private final PrintWriter out; // Output stream for sending requests to server

    /**
     * Constructs new Client object and establishes connection to server.
     *
     * @param serverAddress Address of server to connect to
     * @param port Port number to connect on
     * @throws IOException - If an I/O error occurs when creating socket
     */
    public Client(String serverAddress, int port) throws IOException {
        this.socket = new Socket(serverAddress, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * Sends request to server and waits for response.
     *
     * @param request Request string to send to server
     * @return Response string received from server
     * @throws IOException - If an I/O error occurs while sending or receiving data
     */
    public String sendRequest(String request) throws IOException {
        out.println(request); // Send request to server
        return in.readLine(); // Read and return server's response
    }

    /**
     * Closes connection to server.
     *
     * @throws IOException - If an I/O error occurs while closing socket
     */
    public void close() throws IOException {
        socket.close();
    }
}
