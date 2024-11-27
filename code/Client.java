package code;

import java.io.*;
import java.net.Socket;

public class Client {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public Client(String serverAddress, int port) throws IOException {
        this.socket = new Socket(serverAddress, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public String sendRequest(String request) throws IOException {
        out.println(request);
        return in.readLine();
    }

    public void close() throws IOException {
        socket.close();
    }
}
