package com.perosa.avatarbot.core.payload;

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

        PayloadParser parser = new PayloadParser();

        assertEquals(true, parser.isWelcomeIntent(request));
    }

    @Test
    public void getUserText() {
        LOGGER.info("getUserText");

        GoogleCloudDialogflowV2WebhookRequest request = load("/hi.json");

        PayloadParser parser = new PayloadParser();

        assertEquals("Hello perosa", parser.getUserText(request));
    }

    @Test
    public void getAction() {
        LOGGER.info("getAction");

        GoogleCloudDialogflowV2WebhookRequest request = load("/funTag.json");

        PayloadParser parser = new PayloadParser();

        assertEquals("tag", parser.getAction(request));
    }

    @Test
    public void getIntentDisplayName() {
        LOGGER.info("getIntentDisplayName");

        GoogleCloudDialogflowV2WebhookRequest request = load("/funTag.json");

        PayloadParser parser = new PayloadParser();

        assertEquals("Fun avatar?", parser.getIntentDisplayName(request));
    }

    @Test
    public void getContext() {
        LOGGER.info("getContext");

        GoogleCloudDialogflowV2WebhookRequest request = load("/hi.json");

        PayloadParser helper = new PayloadParser();

        assertNotNull(helper.getContext(request, "projects/avatarbot-proj/agent/sessions/01/contexts/DefaultWelcomeIntent-followup"));
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
