package com.perosa.avatarbot.controller;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2Context;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2EventInput;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2WebhookRequest;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2WebhookResponse;
import com.perosa.avatarbot.controller.config.Env;
import com.perosa.avatarbot.core.Matcher;
import com.perosa.avatarbot.core.model.Session;
import com.perosa.avatarbot.core.model.SessionStore;
import com.perosa.avatarbot.core.payload.PayloadParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
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
    private SessionStore sessionStore;
    //
    @Autowired
    private Matcher matcher;
    //
    @Autowired
    private Env env;

    private static JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

    @RequestMapping(value = "/avatarbot/ping", method = GET, produces = "text/plain")
    public String ping() {
        LOGGER.info("Ping");
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

        String token = getToken(httpServletRequest);

        if (token == null || !token.equals("Bearer " + getEnv().getToken())) {
            throw new RuntimeException("Unauthorised token: " + token);
        }

        LOGGER.fine("request: " + request);

        String sessionId = request.getSession();

        Session session = getSessionStore().getFrom(sessionId);

        collectTags(request, session);

        LOGGER.info("User says: " + getPayloadParser().getUserText(request));

        if (getPayloadParser().isWelcomeIntent(request)) {
            getSessionStore().addTo(new Session(sessionId));
        } else if (getPayloadParser().getIntentDisplayName(request).equalsIgnoreCase("AllCriteriaPassed")) {

            LOGGER.fine("tags: " + session.getTags());

            String avatar = getMatcher().match(session.getTags(), getHost(httpServletRequest));

            if (avatar != null) {
                Map<String, Object> outputContextParameters = new HashMap<>();
                outputContextParameters.put("avatarUrl", avatar);
                setOutputContext(sessionId + "/contexts/avatar", outputContextParameters, response);

                GoogleCloudDialogflowV2EventInput eventInput = new GoogleCloudDialogflowV2EventInput();
                eventInput.setName("EV_FOUND");
                response.setFollowupEventInput(eventInput);

                LOGGER.info("EV_FOUND: " + avatar);

            } else {
                GoogleCloudDialogflowV2EventInput eventInput = new GoogleCloudDialogflowV2EventInput();
                eventInput.setName("EV_NOT_FOUND");
                response.setFollowupEventInput(eventInput);

                LOGGER.info("EV_NOT_FOUND");
            }

            // clear to enable new search
            session.getTags().clear();

        } else if (getPayloadParser().getIntentDisplayName(request).equalsIgnoreCase("AvatarNotFound - yes")) {
            setOutputContext(sessionId + "/contexts/DefaultWelcomeIntent-followup", null, response);

            GoogleCloudDialogflowV2EventInput eventInput = new GoogleCloudDialogflowV2EventInput();
            eventInput.setName("EV_TRY_AGAIN");
            response.setFollowupEventInput(eventInput);
        } else if (getPayloadParser().getIntentDisplayName(request).equalsIgnoreCase("AvatarFound - TryAgain")) {
            setOutputContext(sessionId + "/contexts/DefaultWelcomeIntent-followup", null, response);

            GoogleCloudDialogflowV2EventInput eventInput = new GoogleCloudDialogflowV2EventInput();
            eventInput.setName("EV_TRY_AGAIN");
            response.setFollowupEventInput(eventInput);
        }

        LOGGER.fine("response->" + response);

        return response;

    }

    private void collectTags(GoogleCloudDialogflowV2WebhookRequest request, Session session) {

        String tag = null;

        String action = getPayloadParser().getAction(request);
        if (action != null && action.equalsIgnoreCase("tag")) {
            tag = getPayloadParser().getUserText(request);
        }

        if (session != null && tag != null) {
            session.addToTags(tag.toLowerCase());
        }


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

    private void setOutputContext(String contextName, Map<String, Object> outputContextParameters, GoogleCloudDialogflowV2WebhookResponse response) {

        GoogleCloudDialogflowV2Context outputContext = new GoogleCloudDialogflowV2Context();
        outputContext.setName(contextName);
        outputContext.setLifespanCount(5);

        if(outputContextParameters != null && !outputContextParameters.isEmpty()) {
            outputContext.setParameters(outputContextParameters);
        }

        response.getOutputContexts().add(outputContext);
    }

    private String getHost(HttpServletRequest httpServletRequest) {
        return "https://" + httpServletRequest.getServerName();
    }

    private void verify(HttpServletRequest request) {

        String header = getToken(request);

        if (header == null || !header.equals(getEnv().getToken())) {
            throw new RuntimeException("Invalid token: " + header);
        }

    }

    private String getToken(HttpServletRequest request) {
        String token = null;

        Enumeration headers = request.getHeaderNames();

        if (headers.hasMoreElements()) {
            do {
                String header = (String) headers.nextElement();

                if (header.equalsIgnoreCase("authorization")) {
                    token = request.getHeader(header);
                    break;
                }

            } while (headers.hasMoreElements());
        }

        return token;
    }

    public PayloadParser getPayloadParser() {
        return payloadParser;
    }

    public SessionStore getSessionStore() {
        return sessionStore;
    }

    public Env getEnv() {
        return env;
    }

    public Matcher getMatcher() {
        return matcher;
    }

}
