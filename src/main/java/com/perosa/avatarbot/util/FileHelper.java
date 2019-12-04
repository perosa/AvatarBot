package com.perosa.avatarbot.util;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Random;

@Service
public class FileHelper {

    public File getRandomFile(String path) {

        File[] files = getFiles(path);

        return files[new Random().nextInt(files.length)];
    }

    File[] getFiles(String path) {

        File[] matchingFiles = new File(path).listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith("png") || name.endsWith("jpg");
            }
        });

        return matchingFiles;
    }
}
