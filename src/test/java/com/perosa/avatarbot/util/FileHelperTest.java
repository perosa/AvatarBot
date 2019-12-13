package com.perosa.avatarbot.util;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

class FileHelperTest {

    @Test
    void getRandomFile() {
        FileHelper fileHelper = new FileHelper();
        assertNotNull(fileHelper.getRandomFile("src/main/resources/avatars/professional/male"));
    }

    @Test
    void getFiles() {
        FileHelper fileHelper = new FileHelper();
        assertTrue(fileHelper.getFiles("src/main/resources/avatars/professional/male").length > 0);
    }

    @Test
    public void getContent() {

        FileHelper fileHelper = new FileHelper();
        assertNotNull(fileHelper.getContent("src/main/resources/avatars/professional/male/man.png"));
    }

    @Test
    public void getUrl() {

        FileHelper fileHelper = new FileHelper();
        assertEquals("http://localhost/avatarbot/get/dir/man.png", fileHelper.getUrl("http://localhost","dir/man.png"));
    }
}