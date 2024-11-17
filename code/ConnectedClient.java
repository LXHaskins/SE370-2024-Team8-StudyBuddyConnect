package code;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectedClient {
    private Socket clientSocket;
    private DataInputStream in;

    public ConnectedClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            this.in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readMessages() {
        String line = "";       // Variable to store each incoming message
        while(!line.equals(Server.STOP_STRING)) { // Continue reading until stop string is received
            try {
                line = in.readUTF();           // Read a message from client
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(line);      // Print the message to console
        }
    }

    public void close() {
        try {
            clientSocket.close();
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
