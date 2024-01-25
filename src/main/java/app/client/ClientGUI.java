package app.client;

import translator.Language;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ClientGUI extends JFrame {
    private Client client;
    private JTextField inputField;
    private JTextArea responseArea;
    private JComboBox<Language> sourceLanguageComboBox;
    private JComboBox<Language> targetLanguageComboBox;

    public static void main(String[] args) {
        Client client = new Client();
        ClientGUI clientGUI = new ClientGUI(client);
        clientGUI.setVisible(true);

        String host = "localhost";
        int port = 8000;
        try {
            client.connect(host, port);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }

    public ClientGUI(Client client) {
        this.client = client;

        setLayout(new BorderLayout()); // Changed to BorderLayout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);

        JLabel topLabel = new JLabel("Java Translator");
        topLabel.setFont(new Font(topLabel.getFont().getFontName(), topLabel.getFont().getStyle(), topLabel.getFont().getSize() * 3));
        add(topLabel, BorderLayout.NORTH); // Add label to the top

        JPanel centerPanel = new JPanel(new GridLayout(1, 2));

        inputField = new JTextField();
        JButton sendButton = new JButton("Send");
        responseArea = new JTextArea();
        responseArea.setEditable(false);

        sourceLanguageComboBox = new JComboBox<>();
        targetLanguageComboBox = new JComboBox<>();
        sourceLanguageComboBox = new JComboBox<>();
        sourceLanguageComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Language) {
                    setText(((Language) value).getLanguageName());
                }
                return this;
            }
        });

        targetLanguageComboBox = new JComboBox<>();
        targetLanguageComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Language) {
                    setText(((Language) value).getLanguageName());
                }
                return this;
            }
        });

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
                    Language sourceLanguage = (Language) sourceLanguageComboBox.getSelectedItem();
                    Language targetLanguage = (Language) targetLanguageComboBox.getSelectedItem();
                    String text = inputField.getText();
                    try {
                        client.sendText(sourceLanguage.getLanguageCode());
                        client.sendText(targetLanguage.getLanguageCode());
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

    public void updateLanguageComboBoxes() {
        // Remove all items from the combo boxes
        sourceLanguageComboBox.removeAllItems();
        targetLanguageComboBox.removeAllItems();

        // Fetch the available languages from the server
        List<Language> languages = client.getAvailableLanguagesList();

        // Add the available languages to the combo boxes
        for (Language language : languages) {
            sourceLanguageComboBox.addItem(language);
            targetLanguageComboBox.addItem(language);
        }
    }
}