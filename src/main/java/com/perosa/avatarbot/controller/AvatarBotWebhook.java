package com.perosa.avatarbot.controller;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2Context;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2WebhookRequest;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2WebhookResponse;
import com.perosa.avatarbot.model.Session;
import com.perosa.avatarbot.model.SessionStore;
import com.perosa.avatarbot.payload.PayloadParser;
import com.perosa.avatarbot.util.ApplicationProperty;
import com.perosa.avatarbot.util.FileHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class AvatarBotWebhook {

    private static final Logger LOGGER = Logger.getLogger(AvatarBotWebhook.class.getName());
    //

    @Autowired
    private PayloadParser payloadParser;
    //
    @Autowired
    private FileHelper fileHelper;
    //
    @Autowired
    private ApplicationProperty applicationProperty;
    //
    @Autowired
    private SessionStore sessionStore;

    private static JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

    @RequestMapping(value = "/avatarbot/test", method = GET, produces = "text/plain")
    public String test() {
        LOGGER.info("test URL");
        return "ok";
    }

    @RequestMapping(value = "/avatarbot/post", method = POST)
    @ResponseBody
    public GoogleCloudDialogflowV2WebhookResponse handler(HttpServletRequest httpServletRequest, @RequestBody String body) {

        GoogleCloudDialogflowV2WebhookRequest request = null;
        GoogleCloudDialogflowV2WebhookResponse response = null;

        request = extractGoogleCloudDialogflowV2WebhookRequest(body);

        response = new GoogleCloudDialogflowV2WebhookResponse();
        response.setOutputContexts(new ArrayList<>());

        LOGGER.info("request: " + request);

        if (request == null) {
            throw new RuntimeException("Request is null");
        }

        String sessionId = request.getSession();
        String input = getPayloadParser().getUserText(request);

        if (getPayloadParser().isWelcomeIntent(request)) {
            getSessionStore().addTo(new Session(sessionId));
        } else if (getPayloadParser().getIntentDisplayName(request).equalsIgnoreCase("AllCriteriaPassed")) {
            // followUp Welcome to re-trigger the Welcome Intent
//            GoogleCloudDialogflowV2EventInput googleCloudDialogflowV2EventInput = new GoogleCloudDialogflowV2EventInput();
//            googleCloudDialogflowV2EventInput.setName("getAvatar");
//            response.setFollowupEventInput(googleCloudDialogflowV2EventInput);

            String path = "src/main/resources/avatars/professional/male";

            setOutputContext(sessionId + "/contexts/avatar",
                    getFileHelper().getUrl(getHost(httpServletRequest), getFileHelper().getRandomFile(path)), response);
        } else {

            Session session = getSessionStore().getFrom(sessionId);
            if (session == null) {
                throw new RuntimeException("Session is null");
            }

            String action = getPayloadParser().getAction(request);
            if (action != null && action.equalsIgnoreCase("tag")) {
                session.addToTags(input);
            }
        }

        LOGGER.fine("response : " + response);

        return response;

    }

    private GoogleCloudDialogflowV2WebhookRequest extractGoogleCloudDialogflowV2WebhookRequest(String body) throws RuntimeException {

        GoogleCloudDialogflowV2WebhookRequest googleCloudDialogflowV2WebhookRequest = null;

        LOGGER.finer("body: {0}" + body);

        try {
            googleCloudDialogflowV2WebhookRequest = jacksonFactory.createJsonParser(body)
                    .parse(GoogleCloudDialogflowV2WebhookRequest.class);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }

        return googleCloudDialogflowV2WebhookRequest;
    }

    private void setOutputContext(String contextName, String avatarUrl, GoogleCloudDialogflowV2WebhookResponse response) {
        LOGGER.fine("setOutputContext sessionId:" + contextName);

        GoogleCloudDialogflowV2Context outputContext = new GoogleCloudDialogflowV2Context();
        outputContext.setName(contextName);
        outputContext.setLifespanCount(5);
        Map<String, Object> outputContextParameters = new HashMap<>();

        outputContextParameters.put("avatarUrl", avatarUrl);
        outputContext.setParameters(outputContextParameters);

        response.getOutputContexts().add(outputContext);

    }

    private String getHost(HttpServletRequest httpServletRequest) {
        return "https://" + httpServletRequest.getServerName();
    }

    public PayloadParser getPayloadParser() {
        return payloadParser;
    }

    public void setPayloadParser(PayloadParser payloadParser) {
        this.payloadParser = payloadParser;
    }

    public FileHelper getFileHelper() {
        return fileHelper;
    }

    public void setFileHelper(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    public SessionStore getSessionStore() {
        return sessionStore;
    }

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }
}
