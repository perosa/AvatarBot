package com.perosa.avatarbot.core;

import com.perosa.avatarbot.controller.config.Env;
import com.perosa.avatarbot.util.FileHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class Matcher {

    private static final Logger LOGGER = Logger.getLogger(Matcher.class.getName());

    @Autowired
    private FileHelper fileHelper;
    @Autowired
    private Env env;

    public String match(List<String> tags, String host) {
        LOGGER.fine("match " + tags);

        String ret = null;

        String file = getFileHelper().getRandomFile(getPath(tags));

        if(file != null) {
            ret = getFileHelper().getUrl(host, file);
        }

        return ret;
    }

    String getPath(List<String> tags) {
        return getEnv().getAvatarsHome() +
                tags.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("/"));
    }

    public FileHelper getFileHelper() {
        return fileHelper;
    }

    public void setFileHelper(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    public Env getEnv() {
        return env;
    }

    public void setEnv(Env env) {
        this.env = env;
    }
}
