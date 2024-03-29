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
    private ObjectInputStream objectIn;
    private List<Integer> wordLengths = new ArrayList<>();

    public ClientHandler(Socket clientSocket, Translator translator) {
        this.clientSocket = clientSocket;
        this.translator = translator;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
            List<Language> availableLanguages = translator.getAvailableLanguagesList();
            objectOut.writeObject(availableLanguages);
            objectOut.flush();
            handleClientRequest();
        } catch (IOException e) {
            System.out.println("Error handling client request: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleClientRequest() throws IOException, ClassNotFoundException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        ObjectInputStream objectIn = new ObjectInputStream(clientSocket.getInputStream());
        Language sourceLanguage = null;
        Language targetLanguage = null;
//        String sourceLanguageCode;
//        String targetLanguageCode;
        String text;
        do {
            sourceLanguage = (Language) objectIn.readObject();
            targetLanguage = (Language) objectIn.readObject();
            text = in.readLine();
            if (text != null && !text.equalsIgnoreCase("exit")) {
//                Language sourceLanguage = new Language(sourceLanguageCode);
//                Language targetLanguage = new Language(targetLanguageCode);
                String translatedText = translator.translate(text, sourceLanguage, targetLanguage);
                out.println(sourceLanguage.getLanguageCode() + " -> " + targetLanguage.getLanguageCode() + ": " + text + " -> " + translatedText);

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