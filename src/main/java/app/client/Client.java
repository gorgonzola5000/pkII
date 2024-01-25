package app.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ClientGUI clientGUI;
    public static void main(String[] args) {
        Client client = new Client();
        System.out.println("Specify server IP address: ");
        String host = "localhost";
        //String host = scanner.nextLine();
        System.out.println("Specify server port: ");
        int port = 8000;
        //int port = scanner.nextInt();
        try {
            client.connect(host, port); // replace with your host, port, and text
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }

    public Client() {
        this.clientGUI = new ClientGUI(this);
        clientGUI.setVisible(true);
    }

    public void connect(String host, int port) throws IOException {
        if (socket == null) {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        Scanner scanner = new Scanner(System.in);
        String text;
        do {
            System.out.println("Enter text to translate (or 'exit' to quit): ");
            text = scanner.nextLine();

            if (!text.equalsIgnoreCase("exit")) {
                out.println(text);
                String translatedText = in.readLine();
                System.out.println("Translated text: " + translatedText);

                String statistics = in.readLine();
                System.out.println(statistics);
            }
        } while (!text.equalsIgnoreCase("exit"));
    }

    public void sendText(String text) throws IOException {
        if (out != null) {
            out.println(text);
        }
    }

    public String getResponse() throws IOException {
        if (in != null) {
            String translatedText = in.readLine();
            String statistics = in.readLine();
            return "Translated text: " + translatedText + "\n" + statistics;
        }
        return null;
    }

    public void disconnect() throws IOException {
        if (socket != null) {
            socket.close();
            socket = null;
        }
        if (out != null) {
            out.close();
            out = null;
        }
        if (in != null) {
            in.close();
            in = null;
        }
    }
}