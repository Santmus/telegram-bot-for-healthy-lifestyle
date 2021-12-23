package com.example.student.BSUIR.HealthyLifestyleBot.Service.Realization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Translator {
    /*example code
    * original:Привет, мир!!!
    * translated: Hello World */
    public static void main(String[] args) throws IOException {
        String text = "Привет мир!!!";
        System.out.println("Translated text: " + translate("ru", "ja", text));
    }

    public static String translate(String langFrom, String langTo, String text) throws IOException, IOException {
        // INSERT YOU URL HERE
        String urlStr = "https://script.google.com/macros/s/AKfycbyxlU5yemW5Ls3i3oiF2F08foRlwTJKYAIcwZFpQCA7UiqDg2Q/exec" +
                "?q=" + URLEncoder.encode(text, StandardCharsets.UTF_8) +
                "&target=" + langTo +
                "&source=" + langFrom;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
