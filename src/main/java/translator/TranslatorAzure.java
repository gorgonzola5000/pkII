package translator;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import app.server.ConfigReader;

public class TranslatorAzure implements Translator {
    private static String key;

    // location, also known as region.
    // required if you're using a multiservice or regional (not global) resource. It can be found in the Azure portal on the Keys and Endpoint page.
    private static String location;
    private static String endpoint;

    private List<Language> availableLanguagesList;
    // Instantiates the OkHttpClient.
    private final OkHttpClient client = new OkHttpClient();

    public TranslatorAzure() throws IOException {
        ConfigReader configReader = new ConfigReader();
        key = configReader.getSecret("AZURE_TRANSLATOR_API_KEY");
        location = configReader.getSecret("AZURE_RESOURCE_LOCATION");
        endpoint = configReader.getSecret("ENDPOINT");
        loadAvailableLanguages();
    }

    @Override
    public String translate(String text, Language languageFrom, Language languageTo) throws IOException {
        Gson gson = new Gson();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create("[{\"Text\": \"" + text + "\"}]", mediaType);
        // Add query parameters to the URL
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(endpoint)).newBuilder();
        urlBuilder.addPathSegment("/translate");
        urlBuilder.addQueryParameter("api-version", "3.0");
        urlBuilder.addQueryParameter("from", languageFrom.getLanguageCode());
        urlBuilder.addQueryParameter("to", languageTo.getLanguageCode());

        String urlWithQueryParams = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(urlWithQueryParams)
                .post(body)
                .addHeader("Ocp-Apim-Subscription-Key", key)
                .addHeader("Ocp-Apim-Subscription-Region", location)
                .addHeader("Content-type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            Type listType = new TypeToken<List<TranslationResponse>>(){}.getType();
            List<TranslationResponse> translationResponses = gson.fromJson(response.body().string(), listType);
            return translationResponses.getFirst().translations.getFirst().text;
        }
    }

    private void loadAvailableLanguages() throws IOException {
        Gson gson = new Gson();
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(endpoint)).newBuilder();
        urlBuilder.addPathSegment("/languages");
        urlBuilder.addQueryParameter("api-version", "3.0");
        urlBuilder.addQueryParameter("scope", "translation");

        String urlWithQueryParams = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(urlWithQueryParams)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;

            String responseBody = response.body().string();

            JsonElement jsonElement = JsonParser.parseString(responseBody);
            JsonObject jsonResponse = jsonElement.getAsJsonObject();
            JsonObject translationObject = jsonResponse.getAsJsonObject("translation");
            Map<String, JsonObject> languageMap = gson.fromJson(translationObject, new TypeToken<Map<String, JsonObject>>() {}.getType());

            // Convert the map to a list and create a new Language object for each entry
            availableLanguagesList = new ArrayList<>();
            for (Map.Entry<String, JsonObject> entry : languageMap.entrySet()) {
                String languageCode = entry.getKey();
                JsonObject languageObject = entry.getValue();
                String languageName = languageObject.get("name").getAsString();
                String nativeLanguageName = languageObject.get("nativeName").getAsString();
                Language newLanguage = new Language(languageCode, languageName, nativeLanguageName);
                availableLanguagesList.add(newLanguage);
            }
        }
    }
    public List<Language> getAvailableLanguagesList() {
        return availableLanguagesList;
    }
    static class Translation {
        String text;
        String to;
    }

    static class TranslationResponse {
        List<Translation> translations;
    }

}