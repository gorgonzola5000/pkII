package app.server;

import translator.Language;
import translator.Translator;
import translator.TranslatorAzure;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(8000); // replace 8000 with your desired port number
        } catch (IOException e) {
            System.out.println("Error starting server: " + e.getMessage());
        }
    }
    private Translator translator;

    private Server() {
        try {
            translator = new TranslatorAzure();
        } catch (IOException e) {
            System.out.println("Error initializing TranslatorAzure: " + e.getMessage());
        }
    }

    public void start(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            handleClientRequest(clientSocket);
        }
    }

    private void handleClientRequest(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        String text = in.readLine();
        String translatedText = translator.translate(text, new Language("en"), new Language("fr"));

        out.println(translatedText);
    }
}