package code;

public class Main {
    public static void main(String[] args) {
        Client client = new Client(); // Create the client instance
        MainWindow mainWindow = new MainWindow(client); // Pass the client to MainWindow
        mainWindow.show();
    }
}
