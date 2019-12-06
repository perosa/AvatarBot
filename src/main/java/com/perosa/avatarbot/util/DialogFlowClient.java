package com.perosa.avatarbot.util;

import com.google.api.gax.core.CredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import com.google.common.collect.Lists;
import com.perosa.avatarbot.controller.config.Env;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DialogFlowClient {

    private static final Logger LOGGER = Logger.getLogger(DialogFlowClient.class.getName());

    private static final String GOOGLE_CR_SCOPE = "https://www.googleapis.com/auth/cloud-platform";

    @Autowired
    private Env env;


    public DetectIntentResponse detectIntentWithText(String projectId, String text, String sessionId) {


        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    LOGGER.warning(ex.getMessage());
                }

                DetectIntentResponse response = null;
                try {

                    try (SessionsClient sessionsClient = SessionsClient.create(getSessionsSettings())) {
                        SessionName session = SessionName.of(projectId, sessionId);

                        TextInput.Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode("en-us");
                        QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

                        response = sessionsClient.detectIntent(session, queryInput);

                    }

                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error while sending Text", e);
                }
            }
        });
        t.start();

        return null;

    }

    public DetectIntentResponse detectIntentWithEvent(String projectId, String eventName, String sessionId) {

        LOGGER.info("Send event " + eventName + " sessionId " + sessionId);

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    LOGGER.warning(ex.getMessage());
                }

                LOGGER.info("Send event " + eventName + " sessionId " + sessionId);
                DetectIntentResponse response = null;
                try {

                    try (SessionsClient sessionsClient = SessionsClient.create(getSessionsSettings())) {
                        SessionName session = SessionName.of(projectId, sessionId);

                        EventInput.Builder eventInput = EventInput.newBuilder().setName(eventName).setLanguageCode("en-us");
                        QueryInput queryInput = QueryInput.newBuilder().setEvent(eventInput).build();

                        response = sessionsClient.detectIntent(session, queryInput);

                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error while sending Event", e);
                }

            }
        });
        t.start();

        return null;

    }

    private SessionsSettings getSessionsSettings() throws Exception {
        InputStream is = DialogFlowClient.class.getResourceAsStream(getEnv().getGoogleServiceAccountJsonFile());
        GoogleCredentials credentials = GoogleCredentials.fromStream(is)
                .createScoped(Lists.newArrayList(GOOGLE_CR_SCOPE));
        return SessionsSettings.newBuilder().setCredentialsProvider(new MyCredentialsProvider(credentials)).build();
    }

    private class MyCredentialsProvider implements CredentialsProvider {
        GoogleCredentials credentials;

        public MyCredentialsProvider(GoogleCredentials credentials) {
            this.credentials = credentials;
        }

        @Override
        public Credentials getCredentials() throws IOException {
            return credentials;
        }
    }

    public Env getEnv() {
        return env;
    }

}
