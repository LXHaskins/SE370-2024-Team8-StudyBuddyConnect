package code;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectedClient {
    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;

    public ConnectedClient(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        this.out = new DataOutputStream(clientSocket.getOutputStream());
    }

    /**
     * Reads the first message from the client (LOGIN or REGISTER).
     */
    public String readInitialMessage() throws IOException {
        return in.readUTF();
    }

    /**
     * Sends a message to the client.
     */
    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }

    /**
     * Closes the client connection and associated streams.
     */
    public void close() throws IOException {
        clientSocket.close();
        in.close();
        out.close();
    }
}
