package translator;
public class Language {
    private String languageCode;
    private String nativeLanguageName;
    private String languageName;
    public Language(String languageCode){
        this.languageCode = languageCode;
    }
    public Language(String languageCode, String languageName, String nativeLanguageName){
        this.languageCode = languageCode;
        this.languageName = languageName;
        this.nativeLanguageName = nativeLanguageName;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }
    public void setNativeLanguageName(String nativeLanguageName) {
        this.nativeLanguageName = nativeLanguageName;
    }
    public String getLanguageCode() {
        return languageCode;
    }

    public String getNativeLanguageName() {
        return nativeLanguageName;
    }

    public String getLanguageName() {
        return languageName;
    }
}
