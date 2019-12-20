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
import java.util.Map;
import java.util.logging.Logger;

@Service
public class DashbotAgent {

    private static final Logger LOGGER = Logger.getLogger(DashbotAgent.class.getName());

    public void call(String apiKey, String payload) {

        String endpoint = "https://dashbotconnector.herokuapp.com/send";

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

            connection.setRequestProperty("DASHBOT_API_KEY", apiKey);
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

    void setHeaders(HttpURLConnection connection, Map<String, String> headers) {
        if (headers.get("Content-Type") != null) {
            connection.setRequestProperty("Content-Type", headers.get("Content-Type"));
        }
        if (headers.get("Accept") != null) {
            connection.setRequestProperty("Accept", headers.get("Accept"));
        }
        if (headers.get("Host") != null) {
            connection.setRequestProperty("Host", headers.get("Host"));
        }
        if (headers.get("Accept-Encoding") != null) {
            connection.setRequestProperty("Accept-Encoding", headers.get("Accept-Encoding"));
        }
        if (headers.get("User-Agent") != null) {
            connection.setRequestProperty("User-Agent", headers.get("User-Agent"));
        }
        if (headers.get("Authorization") != null) {
            connection.setRequestProperty("Authorization", headers.get("Authorization"));
        }
    }

}
