package com.perosa.avatarbot.controller.config;

import org.springframework.stereotype.Service;

@Service
public class Env {

    public String getAvatarsHome() {
        String home = System.getenv("AVATARBOT_HOME");

        if (home == null || home.isEmpty()) {
            home = "src/main/resources/avatars/";
        }

        return home;
    }


}
