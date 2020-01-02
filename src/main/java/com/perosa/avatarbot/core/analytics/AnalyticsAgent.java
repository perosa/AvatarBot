package com.perosa.avatarbot.core.analytics;

import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@Service
public class AnalyticsAgent {

    private static final Logger LOGGER = Logger.getLogger(AnalyticsAgent.class.getName());

    public void call(String endpoint, String apiKey, String payload) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {

            URL url = new URL(endpoint);

            if (url.getProtocol().equalsIgnoreCase("https")) {
                connection = (HttpsURLConnection) url.openConnection();
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }
            //  Set connection properties
            connection.setRequestMethod("POST");
            connection.setReadTimeout(3 * 1000);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestProperty("API_KEY", apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            if (payload != null) {
                OutputStream os = connection.getOutputStream();

                os.write(payload.getBytes(StandardCharsets.UTF_8));

                os.flush();
                os.close();
            }

            int responseCode = connection.getResponseCode();

            if (responseCode != 0) {

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder stringBuilder = new StringBuilder();
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }

                LOGGER.info("responseCode: " + responseCode + " response: " + stringBuilder);


            } else {
                LOGGER.severe("responseCode:" + responseCode + " responseMessage:" + connection.getResponseMessage());
                throw new RuntimeException("responseCode:" + responseCode + " message:" + connection.getResponseMessage());
            }

        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

}
