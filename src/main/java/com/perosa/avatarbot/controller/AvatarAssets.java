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
     * @param filename name of the resource
     * @return
     */
    @RequestMapping(value = "/avatarbot/view/{filename:.+}", method = GET)
    public ResponseEntity<byte[]> viewResource(@PathVariable String filename,
                                               HttpServletRequest httpServletRequest) {
        LOGGER.info("viewResource filename:" + filename);

        byte[] byteFileContent = getFileHelper().getContent(filename);

        if (byteFileContent == null) {
            LOGGER.warning("File Not Found: " + filename);
            return getErrorResponse("Resource Not Found: " + filename);
        }

        MediaType mediaType = MediaType.IMAGE_PNG;

        if (filename.endsWith(".jpg")) {
            mediaType = MediaType.IMAGE_JPEG;
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

    public FileHelper getFileHelper() {
        return fileHelper;
    }

    public void setFileHelper(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

}

