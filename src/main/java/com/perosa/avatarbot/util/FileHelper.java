package com.perosa.avatarbot.util;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.Random;
import java.util.logging.Logger;

@Service
public class FileHelper {

    private static final Logger LOGGER = Logger.getLogger(FileHelper.class.getName());

    public String getRandomFile(String path) {

        String filepath = null;

        File[] files = getFiles(path);

        if(files != null && files.length > 0) {
            filepath = files[new Random().nextInt(files.length)].getAbsolutePath()
                    .replace("/", "---")
                    .replace("\\", "---");
        }

        LOGGER.fine("getRandomFile filepath: " + filepath);

        return filepath;
    }

    File[] getFiles(String path) {

        File[] matchingFiles = new File(path).listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith("png") || name.endsWith("jpg");
            }
        });

        return matchingFiles;
    }

    public byte[] getContent(String filepath) {

        filepath = filepath.replace("---", "/");

        byte[] byteFileContent = null;

        try (InputStream is = new FileInputStream(filepath)) {
            byteFileContent = IOUtils.toByteArray(is);
        } catch (Exception e) {
            LOGGER.severe("ERROR File Not Found: " + e.getMessage());
        }

        return byteFileContent;
    }

    public String getUrl(String host, String file) {
        return host + "/avatarbot/view/" + file;
    }
}
