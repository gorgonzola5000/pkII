package app;

import translator.Language;
import translator.Translator;
import translator.TranslatorAzure;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");

        Translator translator = new TranslatorAzure();
        String text = translator.translate("I hate this university", new Language("en"), new Language("fr"));
        System.out.println(text);
        String text2 = translator.translate("I can't do this anymore", new Language("en"), new Language("pl"));
        System.out.println(text2);
        System.out.println(text);
        translator.getAvailableLanguagesList().forEach(language -> System.out.println(language.getLanguageName()+": "+language.getLanguageCode()));
    }
}