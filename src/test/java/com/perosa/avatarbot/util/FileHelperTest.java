package com.perosa.avatarbot.util;

import org.junit.jupiter.api.Test;

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
}