package com.perosa.avatarbot.util;

import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DialogFlowClientTest {

    private static final Logger LOGGER = Logger.getLogger(DialogFlowClient.class.getName());

    @Resource
    DialogFlowClient dialogFlowClient;

    @Test
    public void detectIntentWithText() throws Exception {

        DetectIntentResponse result = dialogFlowClient.detectIntentWithText( "brandmybot-qaqudq", "Hi", "01");

        LOGGER.fine("res->" + result);

        assertNotNull(result);

    }

    @Test
    public void detectIntentWithEvent() throws Exception {

        DetectIntentResponse result = dialogFlowClient.detectIntentWithEvent( "brandmybot-qaqudq", "avatarReady", "01");

        LOGGER.fine("res->" + result);

        assertNotNull(result);

    }

}