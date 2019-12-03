package com.perosa.avatarbot.payload;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2WebhookRequest;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class PayloadParserTest {

    private static final Logger LOGGER = Logger.getLogger(PayloadParserTest.class.getName());

    @Test
    public void isWelcome() {
        LOGGER.info("isWelcome");

        GoogleCloudDialogflowV2WebhookRequest request = load("/hi.json");

        PayloadParser helper = new PayloadParser();

        assertEquals(true, helper.isWelcomeIntent(request));
    }

    @Test
    public void getContext() {
        LOGGER.info("getContext");

        GoogleCloudDialogflowV2WebhookRequest request = load("/hi.json");

        PayloadParser helper = new PayloadParser();

        assertNotNull(helper.getContext(request, "projects/brandmybot-qaqudq/agent/sessions/01/contexts/DefaultWelcomeIntent-followup"));
    }


    private GoogleCloudDialogflowV2WebhookRequest load(String filename) {

        GoogleCloudDialogflowV2WebhookRequest request;

        InputStream inputStream = getClass().getResourceAsStream(filename);

        try {
            request = JacksonFactory.getDefaultInstance().createJsonParser(inputStream)
                    .parse(GoogleCloudDialogflowV2WebhookRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return request;
    }

}
