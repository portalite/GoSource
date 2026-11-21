
package com.kaan.gosource;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.concurrent.Executors;

public class ResearchEngine {

    public interface Listener {
        void onResult(String result);
        void onError(String error);
    }

    private static String request(String address)
            throws Exception {

        HttpURLConnection connection =
            (HttpURLConnection) new URL(address).openConnection();

        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(15000);
        connection.setRequestProperty(
            "User-Agent", "GoSource/1.0"
        );

        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    connection.getInputStream(), "UTF-8"
                )
            );

            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            reader.close();
            return result.toString();

        } finally {
            connection.disconnect();
        }
    }

    public static void search(
            String topic, Listener listener) {

        Executors.newSingleThreadExecutor().execute(() -> {

            try {
                String query = URLEncoder.encode(
                    topic, "UTF-8"
                );

                String searchUrl =
                    "https://tr.wikipedia.org/w/api.php" +
                    "?action=query&list=search" +
                    "&srsearch=" + query +
                    "&format=json&utf8=1&srlimit=5";

                JSONObject searchData =
                    new JSONObject(request(searchUrl));

                JSONArray results = searchData
                    .getJSONObject("query")
                    .getJSONArray("search");

                StringBuilder report = new StringBuilder();

                report.append("GOSOURCE ARAŞTIRMA SONUCU\n\n");
                report.append("Konu: ").append(topic);
                report.append("\n\n");

                int count = 0;

                for (int i = 0; i < results.length(); i++) {

                    JSONObject item = results.getJSONObject(i);

                    long pageId = item.optLong("pageid");
                    String title = item.optString("title");

                    String contentUrl =
                        "https://tr.wikipedia.org/w/api.php" +
                        "?action=query&prop=extracts" +
                        "&explaintext=1&pageids=" + pageId +
                        "&format=json";

                    JSONObject pageData =
                        new JSONObject(request(contentUrl));

                    JSONObject pages = pageData
                        .getJSONObject("query")
                        .getJSONObject("pages");

                    Iterator<String> keys = pages.keys();

                    if (!keys.hasNext()) continue;

                    JSONObject page = pages.getJSONObject(
                        keys.next()
                    );

                    String extract = page.optString("extract")
                        .replaceAll("\\s+", " ").trim();

                    if (extract.isEmpty()) continue;

                    String[] words = extract.split("\\s+");

                    StringBuilder quote = new StringBuilder();

                    for (int w = 0; w < Math.min(25, words.length); w++) {
                        quote.append(words[w]).append(" ");
                    }

                    count++;

                    report.append("KAYNAK ").append(count);
                    report.append("\n").append(title);
                    report.append("\n\nKısa alıntı:\n");
                    report.append("“").append(quote.toString().trim());
                    report.append("”\n\n");

                    report.append("Kaynak bağlantısı:\n");
                    report.append(
                        "https://tr.wikipedia.org/?curid="
                    ).append(pageId);

                    report.append("\n\n--------------------\n\n");
                }

                if (count == 0) {
                    report.append("Kaynak bulunamadı.");
                }

                String finalReport = report.toString();

                new Handler(Looper.getMainLooper()).post(
                    () -> listener.onResult(finalReport)
                );

            } catch (Exception e) {

                new Handler(Looper.getMainLooper()).post(
                    () -> listener.onError(
                        "Araştırma sırasında hata oluştu: "
                        + e.getMessage()
                    )
                );
            }
        });
    }
}
