package com.perosa.avatarbot.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationProperty {

    //
    @Value("${avatars.home}")
    private String avatarsHome;

    public String getAvatarsHome() {
        return avatarsHome;
    }

    public void setAvatarsHome(String avatarsHome) {
        this.avatarsHome = avatarsHome;
    }
}
