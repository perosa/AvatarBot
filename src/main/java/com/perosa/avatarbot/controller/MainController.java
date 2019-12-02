package com.perosa.avatarbot.controller;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2Context;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2EventInput;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2WebhookRequest;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2WebhookResponse;
import com.perosa.avatarbot.payload.ContextParser;
import com.perosa.avatarbot.util.ApplicationProperty;
import com.perosa.avatarbot.util.ResourceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class MainController {

    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());
    //
    private static final String AUTH_TOKEN = "*p3r0s}2015I[{]445!+/67;";
    private static final String CONTEXT_NAME = "tags";

    //
    @Autowired
    private ContextParser contextParser;
    //
    @Autowired
    private ResourceHelper resourceHelper;
    //
    @Autowired
    private ApplicationProperty applicationProperty;
    //

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

        HttpSession httpSession = httpServletRequest.getSession();

        LOGGER.info("request: " + request);

        // initialise OutputContexts list
        if(response.getOutputContexts() == null) {
            response.setOutputContexts(new ArrayList<>());
        }

        if (request == null) {
            throw new RuntimeException("Request is null");
        }

        String sessionId = request.getSession();
        String input = getContextParser().getUserText(request);

        LOGGER.info("userText : " + input);

        if (getContextParser().isWelcomeIntent(request)) {
            response.setOutputContexts(new ArrayList<>());
        } else {
            String action = getContextParser().getAction(request);

            if (action != null && action.equalsIgnoreCase("tag")) {
                LOGGER.info("action : " + action);

                GoogleCloudDialogflowV2Context context = getContextParser().getContext(request, CONTEXT_NAME);
                LOGGER.info("context : " + context);

                if(context == null) {
                    context = new GoogleCloudDialogflowV2Context();
                    context.setName(CONTEXT_NAME);
                    context.setLifespanCount(20);
                    Map<String, Object> outputContextParameters = new HashMap<>();
                    context.setParameters(outputContextParameters);
                }

                Map<String, Object> map = context.getParameters();
                map.put(input, "tag");
                LOGGER.info("map : " + map);

                context.setParameters(map);

                LOGGER.info("context : " + context);
                response.getOutputContexts().add(context);


            }
        }


//        if (getContextParser().isFilesContextNotFound(request)) {
//            // Files Context hasnt been set yet
//            LOGGER.fine("Context /files NOT found");
//
//            // build list of URLs
//            String path = getApplicationProperty().getResourcesFolder();
//            String host = "https://" + httpServletRequest.getServerName();
//
//            Map<String, String> map = getResourceHelper().getResourceUrls(path, host, "token");
//
//            // set Files Context
//            //setOutputContext(sessionId + "/contexts/files", map, response);
//
//            // followUp Welcome to re-trigger the Welcome Intent
//            GoogleCloudDialogflowV2EventInput googleCloudDialogflowV2EventInput = new GoogleCloudDialogflowV2EventInput();
//            googleCloudDialogflowV2EventInput.setName("Welcome");
//
//            response.setFollowupEventInput(googleCloudDialogflowV2EventInput);
//
//            LOGGER.info("Response: " + response.toString());
//
//
//            String action = getContextParser().getAction(request);
//
//        }


        return response;

    }

    String process(GoogleCloudDialogflowV2WebhookRequest webHookRequest) {
        String ret = "";

        String action = getContextParser().getAction(webHookRequest);

        if (!action.isEmpty()) {

        }

        if (action.equalsIgnoreCase("DefaultWelcomeIntent.DefaultWelcomeIntent-yes")) {

        } else if (action.equalsIgnoreCase("funTag")) {

        }

        return ret;
    }



    private void setTagsOnContext(Map<String, Object> contextParameters, GoogleCloudDialogflowV2WebhookResponse response) {

        GoogleCloudDialogflowV2Context outputContext = new GoogleCloudDialogflowV2Context();
        outputContext.setName(CONTEXT_NAME);
        outputContext.setLifespanCount(20);
        Map<String, Object> outputContextParameters = new HashMap<>();

        contextParameters.forEach((filename, url) ->
                outputContextParameters.put(filename, url));

        outputContext.setParameters(outputContextParameters);

        response.getOutputContexts().add(outputContext);

    }

    private void setOutputContext(String contextName, Map<String, String> contextParameters, GoogleCloudDialogflowV2WebhookResponse response) {
        LOGGER.fine("setOutputContext sessionId:" + contextName);

        GoogleCloudDialogflowV2Context outputContext = new GoogleCloudDialogflowV2Context();
        outputContext.setName(contextName);
        outputContext.setLifespanCount(20);
        Map<String, Object> outputContextParameters = new HashMap<>();

        contextParameters.forEach((filename, url) ->
                outputContextParameters.put(filename, url));

        outputContext.setParameters(outputContextParameters);

        response.getOutputContexts().add(outputContext);

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

    // verify token from DialogFlow
    private void verifyToken(HttpServletRequest request) {

        String header = getAuthorizationHeader(request);

        if (header == null || !header.equals(AUTH_TOKEN)) {
            throw new RuntimeException("Invalid token: " + header);
        }

    }

    private String getAuthorizationHeader(HttpServletRequest request) {
        String jwt = null;

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();

            if (key.equalsIgnoreCase("authorization")) {
                jwt = request.getHeader(key);
                break;
            }

        }

        return jwt;
    }

    public ContextParser getContextParser() {
        return contextParser;
    }

    public void setContextParser(ContextParser contextParser) {
        this.contextParser = contextParser;
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

}
