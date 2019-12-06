package com.perosa.avatarbot.core;

import com.perosa.avatarbot.util.ApplicationProperty;
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
    private ApplicationProperty applicationProperty;
    @Autowired
    private FileHelper fileHelper;

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
        return getApplicationProperty().getAvatarsHome() +
                tags.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("/"));
    }

    public ApplicationProperty getApplicationProperty() {
        return applicationProperty;
    }

    public void setApplicationProperty(ApplicationProperty applicationProperty) {
        this.applicationProperty = applicationProperty;
    }

    public FileHelper getFileHelper() {
        return fileHelper;
    }

    public void setFileHelper(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }
}
