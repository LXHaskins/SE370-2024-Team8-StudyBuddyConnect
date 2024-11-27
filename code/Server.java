package code;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 5150; // Updated port number
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    public void start() {
        System.out.println("Server started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            CalendarEventDAO eventDAO = new CalendarEventDAO();
            CredentialDAO credentialDAO = new CredentialDAO();
            ServerController serverController = new ServerController(eventDAO, credentialDAO);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(new ConnectedClient(clientSocket, serverController));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
}
