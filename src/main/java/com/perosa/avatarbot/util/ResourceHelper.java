package com.perosa.avatarbot.util;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ResourceHelper {

    private static final Logger LOGGER = Logger.getLogger(ResourceHelper.class.getName());

    public byte[] getResource(String filename) {
        LOGGER.fine("getResource " + filename);

        byte[] byteFileContent = null;

        try (InputStream is = new FileInputStream(filename)) {
            byteFileContent = IOUtils.toByteArray(is);
        } catch (Exception e) {
            LOGGER.severe("ERROR Resource Not Found: " + e.getMessage());
        }

        return byteFileContent;
    }

    /**
     * List resources in the given folder
     *
     * @param path
     * @return
     */
    public List<String> getResources(String path) {
        List<String> list = new ArrayList<>();

        File folder = new File(path);

        if (folder.exists()) {

            Arrays.stream(folder.listFiles()).forEach(
                    element -> list.add(element.getName())
            );
        }

        LOGGER.info("Found " + list.size() + " files in " + path);

        return list;
    }

    /**
     * List of all resource URLs in the given folder
     *
     * @param path  Location of the resources
     * @param host  Server
     * @param token Security token
     * @return
     */
    public Map<String, String> getResourceUrls(String path, String host, String token) {
        Map<String, String> map = null;

        // create map with filename (key) and url (value)
        map = getResources(path).stream()
                .collect(Collectors.toMap(
                        element -> element,
                        element -> {
                            return host + "/avatarbot/view/" + token + "/" + element;
                        }));

        return map;
    }

}
