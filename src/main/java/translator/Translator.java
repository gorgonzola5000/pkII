package translator;

import java.io.IOException;
import java.util.List;

public interface Translator {
    String translate(String textToTranslate, Language languageFrom, Language languageTo) throws IOException;
    List<Language> getAvailableLanguagesList();
    }
