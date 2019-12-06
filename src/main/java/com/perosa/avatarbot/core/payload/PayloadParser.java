package com.perosa.avatarbot.core.payload;

import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2Context;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2QueryResult;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2WebhookRequest;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class PayloadParser {

    private static final Logger LOGGER = Logger.getLogger(PayloadParser.class.getName());

    public boolean isWelcomeIntent(GoogleCloudDialogflowV2WebhookRequest webHookRequest) {
        boolean ret = false;

        String action = getAction(webHookRequest);

        if (action != null && action.equals("input.welcome")) {
            ret = true;
        }

        return ret;
    }

    public String getUserText(GoogleCloudDialogflowV2WebhookRequest webHookRequest) {
        return webHookRequest.getQueryResult().getQueryText();
    }

    public GoogleCloudDialogflowV2Context getContext(GoogleCloudDialogflowV2WebhookRequest webHookRequest, String name) {

        GoogleCloudDialogflowV2Context outputContext = null;

        if (webHookRequest.getQueryResult().getOutputContexts() != null) {
            for (GoogleCloudDialogflowV2Context c : webHookRequest.getQueryResult().getOutputContexts()) {
                if (c.getName().equalsIgnoreCase(name)) {
                    outputContext = c;
                }
            }
        }

        return outputContext;
    }



    /**
     * Retrieves the action name from the matched intent.
     *
     * @param webHookRequest
     * @return action name
     */
    public String getAction(GoogleCloudDialogflowV2WebhookRequest webHookRequest) {
        String action = "";

        GoogleCloudDialogflowV2QueryResult queryResult = webHookRequest.getQueryResult();

        if (queryResult != null) {
            action = queryResult.getAction();
        }

        return action;
    }

    public String getIntentDisplayName(GoogleCloudDialogflowV2WebhookRequest webHookRequest) {
        String action = "";

        GoogleCloudDialogflowV2QueryResult queryResult = webHookRequest.getQueryResult();

        if (queryResult != null) {
            action = queryResult.getIntent().getDisplayName();
        }

        return action;
    }

}
