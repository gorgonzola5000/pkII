package org.example;

import java.io.IOException;

import com.google.gson.*;
import okhttp3.*;

public class TranslatorAzure implements Translator {
    private static String key;

    // location, also known as region.
    // required if you're using a multi-service or regional (not global) resource. It can be found in the Azure portal on the Keys and Endpoint page.
    private static String location;
    private static String endpoint;


    // Instantiates the OkHttpClient.
    OkHttpClient client = new OkHttpClient();

    public TranslatorAzure(){
        ConfigReader configReader = new ConfigReader();
        key = configReader.getSecret("AZURE_TRANSLATOR_API_KEY");
        location = configReader.getSecret("AZURE_RESOURCE_LOCATION");
        endpoint = configReader.getSecret("ENDPOINT");
    }

    // This function performs a POST request.
    public String Post(String text) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create("[{\"Text\": \"" + text + "\"}]", mediaType);
        // Add query parameters to the URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse(endpoint).newBuilder();
        urlBuilder.addPathSegment("/translate");
        urlBuilder.addQueryParameter("api-version", "3.0");
        urlBuilder.addQueryParameter("from", "en");
        urlBuilder.addQueryParameter("to", "fr");

        String urlWithQueryParams = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(urlWithQueryParams)
                .post(body)
                .addHeader("Ocp-Apim-Subscription-Key", key)
                .addHeader("Ocp-Apim-Subscription-Region", location)
                .addHeader("Content-type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        assert response.body() != null;
        return response.body().string();
    }


    // This function prettifies the json response.
    public static String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(json_text);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }

    @Override
    public String translate(String text) {
        String response = null;
        try {
            response = this.Post(text);
//            System.out.println(prettify(response));
        } catch (Exception e) {
            System.out.println(e);
        }
        return response;
    }
}