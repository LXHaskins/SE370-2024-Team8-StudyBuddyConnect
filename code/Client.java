package code;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final int PORT = 5150;

    public boolean login(String username, String password) {
        return sendRequest("LOGIN:" + username + ":" + password).equals("LOGIN_SUCCESS");
    }

    public boolean register(String username, String password) {
        return sendRequest("REGISTER:" + username + ":" + password).equals("REGISTRATION_SUCCESS");
    }

    private String sendRequest(String request) {
        try (Socket socket = new Socket("127.0.0.1", PORT);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            out.writeUTF(request);
            return in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}
