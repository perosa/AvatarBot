package com.perosa.avatarbot.controller;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2EventInput;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2WebhookRequest;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2WebhookResponse;
import com.perosa.avatarbot.model.Session;
import com.perosa.avatarbot.model.SessionStore;
import com.perosa.avatarbot.payload.PayloadParser;
import com.perosa.avatarbot.util.ApplicationProperty;
import com.perosa.avatarbot.util.ResourceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class MainController {

    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());
    //

    @Autowired
    private PayloadParser payloadParser;
    //
    @Autowired
    private ResourceHelper resourceHelper;
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

        //verifyToken(httpServletRequest);

        request = extractGoogleCloudDialogflowV2WebhookRequest(body);
        response = new GoogleCloudDialogflowV2WebhookResponse();

        LOGGER.info("request: " + request);

        if (request == null) {
            throw new RuntimeException("Request is null");
        }

        String sessionId = request.getSession();
        String input = getPayloadParser().getUserText(request);
        String intent = getPayloadParser().getIntentDisplayName(request);

        if (getPayloadParser().isWelcomeIntent(request)) {
            getSessionStore().addTo(new Session(sessionId));
//        } else if (getPayloadParser().getIntentDisplayName(request).equalsIgnoreCase("AllCriteriaPassed")) {
//            // followUp Welcome to re-trigger the Welcome Intent
//            GoogleCloudDialogflowV2EventInput googleCloudDialogflowV2EventInput = new GoogleCloudDialogflowV2EventInput();
//            googleCloudDialogflowV2EventInput.setName("getAvatar");
//            response.setFollowupEventInput(googleCloudDialogflowV2EventInput);
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

        LOGGER.info("session : " + getSessionStore().getFrom(sessionId));
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


    public PayloadParser getPayloadParser() {
        return payloadParser;
    }

    public void setPayloadParser(PayloadParser payloadParser) {
        this.payloadParser = payloadParser;
    }

    public ResourceHelper getResourceHelper() {
        return resourceHelper;
    }

    public void setResourceHelper(ResourceHelper resourceHelper) {
        this.resourceHelper = resourceHelper;
    }

    public ApplicationProperty getApplicationProperty() {
        return applicationProperty;
    }

    public void setApplicationProperty(ApplicationProperty applicationProperty) {
        this.applicationProperty = applicationProperty;
    }

    public SessionStore getSessionStore() {
        return sessionStore;
    }

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }
}
