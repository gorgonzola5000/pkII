package app.client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ClientGUI extends JFrame {
    private Client client;
    private JTextField inputField;
    private JTextArea responseArea;

    public static void main(String[] args) {
        Client client = new Client();
        ClientGUI clientGUI = new ClientGUI();
        clientGUI.setVisible(true);

        String host = "localhost";
        int port = 8000;
        try {
            client.connect(host, port);
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }

    public ClientGUI() {
        this.client = new Client();

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        inputField = new JTextField();
        JButton sendButton = new JButton("Send");
        responseArea = new JTextArea();
        responseArea.setEditable(false);

        sendButton.addActionListener(e -> {
            String text = inputField.getText();
            try {
                client.sendText(text);
                String response = client.getResponse();
                responseArea.append(response + "\n");
            } catch (IOException ioException) {
                responseArea.append("Error: " + ioException.getMessage() + "\n");
            }
        });

        add(inputField, BorderLayout.NORTH);
        add(sendButton, BorderLayout.CENTER);
        add(new JScrollPane(responseArea), BorderLayout.SOUTH);
    }
}