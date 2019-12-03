package com.perosa.avatarbot.controller;

import com.perosa.avatarbot.util.ApplicationProperty;
import com.perosa.avatarbot.util.ResourceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
public class ResourceController {
    private static final Logger LOGGER = Logger.getLogger(ResourceController.class.getName());

    //
    @Autowired
    private ResourceHelper resourceHelper;
    //
    @Autowired
    private ApplicationProperty applicationProperty;
    //


    /**
     * Fetch resources
     *
     * @param filename name of the resource
     * @return
     */
    @RequestMapping(value = "/avatarbot/view/{filename:.+}", method = GET)
    public ResponseEntity<byte[]> viewResource(@PathVariable String filename,
                                               HttpServletRequest httpServletRequest) {
        LOGGER.info("viewResource filename:" + filename);

        long startTime = System.currentTimeMillis();

        byte[] byteFileContent = getResourceHelper().getResource(getApplicationProperty().getResourcesFolder() + "/" + filename);

        if (byteFileContent == null) {
            LOGGER.warning("File Not Found: " + filename);
            return getErrorResponse("Resource Not Found: " + filename);
        }

        // default to videos
        MediaType mediaType = MediaType.valueOf("video/mp4");

        if (filename.endsWith(".png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (filename.endsWith(".jpg")) {
            mediaType = MediaType.IMAGE_JPEG;
        } else if (filename.endsWith(".json")) {
            mediaType = MediaType.APPLICATION_JSON;
        }

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .contentType(mediaType)
                .body(byteFileContent);

    }

    private ResponseEntity<byte[]> getErrorResponse(String msg) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.TEXT_PLAIN)
                .body(msg.getBytes());
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

