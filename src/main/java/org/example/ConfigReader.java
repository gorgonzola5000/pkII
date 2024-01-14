package org.example;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;

    public ConfigReader(){
        this.properties = loadProperties();
    }

    public static void main(String[] args) {
        // Load properties from config.properties file
        properties = loadProperties();
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return properties;
            }

            // Load the properties file
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public String getSecret(String key) {
        return properties.getProperty(key);
    }
}
