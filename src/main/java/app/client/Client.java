package app.client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.connect("localhost", 8000, "Hello, World!"); // replace with your host, port, and text
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }
    private List<Integer> wordLengths = new ArrayList<>();

    public void connect(String host, int port, String text) throws IOException {
        Socket socket = new Socket(host, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.println(text);
        String translatedText = in.readLine();
        System.out.println("Translated text: " + translatedText);

        calculateStatistics(text);
    }

    private void calculateStatistics(String text) {
        String[] words = text.split(" ");
        for (String word : words) {
            wordLengths.add(word.length());
        }

        double averageWordLength = wordLengths.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        System.out.println("Average word length: " + averageWordLength);
    }
}