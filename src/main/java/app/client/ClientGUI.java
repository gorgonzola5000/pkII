package app.client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ClientGUI extends JFrame {
    private Client client;
    private JTextField inputField;
    private JTextArea responseArea;
    private JComboBox<String> sourceLanguageComboBox;
    private JComboBox<String> targetLanguageComboBox;

    public static void main(String[] args) {
        Client client = new Client();
        ClientGUI clientGUI = new ClientGUI(client);
        clientGUI.setVisible(true);

        String host = "localhost";
        int port = 8000;
        try {
            client.connect(host, port);
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }

    public ClientGUI(Client client) {
        this.client = client;

        setLayout(new BorderLayout()); // Changed to BorderLayout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        JLabel topLabel = new JLabel("Your Text Here");
        topLabel.setFont(new Font(topLabel.getFont().getFontName(), topLabel.getFont().getStyle(), topLabel.getFont().getSize() * 3));
        add(topLabel, BorderLayout.NORTH); // Add label to the top

        JPanel centerPanel = new JPanel(new GridLayout(1, 2));

        inputField = new JTextField();
        JButton sendButton = new JButton("Send");
        responseArea = new JTextArea();
        responseArea.setEditable(false);

        String[] languages = {"en", "es", "fr"};
        sourceLanguageComboBox = new JComboBox<>(languages);
        targetLanguageComboBox = new JComboBox<>(languages);

        Dimension comboBoxDimension = new Dimension(200, 40); // Changed height from 20 to 40
        sourceLanguageComboBox.setPreferredSize(comboBoxDimension);
        targetLanguageComboBox.setPreferredSize(comboBoxDimension);

        // Change the font size to 3 times its current size
        Font currentFont = sourceLanguageComboBox.getFont();
        Font titleFont = new Font(currentFont.getFontName(), currentFont.getStyle(), currentFont.getSize() * 3);
        Font contentFont = new Font(currentFont.getFontName(), currentFont.getStyle(), currentFont.getSize() * 2);
        sourceLanguageComboBox.setFont(titleFont);
        targetLanguageComboBox.setFont(titleFont);
        inputField.setFont(contentFont);
        sendButton.setFont(titleFont);
        responseArea.setFont(contentFont);

        sendButton.addActionListener(e -> {
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    String sourceLanguage = (String) sourceLanguageComboBox.getSelectedItem();
                    String targetLanguage = (String) targetLanguageComboBox.getSelectedItem();
                    String text = inputField.getText();
                    try {
                        client.sendText(sourceLanguage);
                        client.sendText(targetLanguage);
                        client.sendText(text);
                        String response = client.getResponse();
                        responseArea.append(response + "\n");
                    } catch (IOException ioException) {
                        responseArea.append("Error: " + ioException.getMessage() + "\n");
                    }
                    return null;
                }
            };
            worker.execute();
        });

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel sourceLabel = new JLabel("Source Language");
        sourceLabel.setFont(titleFont);
        leftPanel.add(sourceLabel);
        leftPanel.add(sourceLanguageComboBox);

        JLabel targetLabel = new JLabel("Target Language");
        targetLabel.setFont(titleFont);
        leftPanel.add(targetLabel);
        leftPanel.add(targetLanguageComboBox);

        leftPanel.add(inputPanel);

        centerPanel.add(leftPanel);
        centerPanel.add(new JScrollPane(responseArea));

        add(centerPanel, BorderLayout.CENTER); // Add centerPanel to the center
    }
}