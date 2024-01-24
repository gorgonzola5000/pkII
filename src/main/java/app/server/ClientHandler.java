package app.server;

import translator.Language;
import translator.Translator;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Translator translator;
    private List<Integer> wordLengths = new ArrayList<>();

    public ClientHandler(Socket clientSocket, Translator translator) {
        this.clientSocket = clientSocket;
        this.translator = translator;
    }

    @Override
    public void run() {
        try {
            handleClientRequest();
        } catch (IOException e) {
            System.out.println("Error handling client request: " + e.getMessage());
        }
    }

    private void handleClientRequest() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        String text;
        do {
            text = in.readLine();
            if (text != null && !text.equalsIgnoreCase("exit")) {
                String translatedText = translator.translate(text, new Language("en"), new Language("fr"));
                out.println(translatedText);

                calculateStatistics(text);
                double averageWordLength = wordLengths.stream().mapToInt(Integer::intValue).average().orElse(0.0);
                out.println("Average word length: " + averageWordLength);
            }
        } while (text != null && !text.equalsIgnoreCase("exit"));
    }

    private void calculateStatistics(String text) {
        String[] words = text.split(" ");
        for (String word : words) {
            wordLengths.add(word.length());
        }
    }
}