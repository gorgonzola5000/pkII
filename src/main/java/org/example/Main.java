package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        // Create TranslatorText with custom initialization lambda
        Translator translatorText = new TranslatorAzure();
        System.out.println(translatorText.translate("I hate horses"));
    }
}