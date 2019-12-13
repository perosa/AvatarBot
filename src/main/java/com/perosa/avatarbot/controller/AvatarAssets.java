package com.perosa.avatarbot.controller;

import com.perosa.avatarbot.util.FileHelper;
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
public class AvatarAssets {
    private static final Logger LOGGER = Logger.getLogger(AvatarAssets.class.getName());

    //
    @Autowired
    private FileHelper fileHelper;
    //

    /**
     * Fetch resources
     *
     * @param name name of the resource
     * @return
     */
    @RequestMapping(value = "/avatarbot/get/{name:.+}", method = GET)
    public ResponseEntity<byte[]> fetch(@PathVariable String name,
                                               HttpServletRequest httpServletRequest) {

        byte[] byteFileContent = getFileHelper().getContent(name);

        if (byteFileContent == null) {
            LOGGER.warning("File Not Found: " + name);
            return getErrorResponse("Resource Not Found: " + name);
        }

        MediaType mediaType = MediaType.IMAGE_PNG;

        if (name.endsWith(".jpg")) {
            mediaType = MediaType.IMAGE_JPEG;
        }

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + name + "\"")
                .contentType(mediaType)
                .body(byteFileContent);

    }

    private ResponseEntity<byte[]> getErrorResponse(String msg) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.TEXT_PLAIN)
                .body(msg.getBytes());
    }

    public FileHelper getFileHelper() {
        return fileHelper;
    }

}

