package com.perosa.avatarbot.controller.config;

import org.springframework.stereotype.Service;

@Service
public class Env {

    public String getProjectId() {
        String projectId = System.getenv("AVATARBOT_PROJECT_ID");

        if (projectId == null || projectId.isEmpty()) {
            projectId = "brandmybot-qaqudq";
        }

        return projectId;
    }

    public String getGoogleServiceAccountJsonFile() {
        String jsonFile = System.getenv("AVATARBOT_SVC_ACCOUNT_JSON_FILE");

        if (jsonFile == null || jsonFile.isEmpty()) {
            jsonFile = "/brandmybot-qaqudq-920c6091174a.json";
        }

        return jsonFile;
    }

}
