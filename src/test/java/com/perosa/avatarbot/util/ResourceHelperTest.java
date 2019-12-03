package com.perosa.avatarbot.util;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.Assert.*;

@Ignore
public class ResourceHelperTest {

    private static final Logger LOGGER = Logger.getLogger(ResourceHelperTest.class.getName());

    @Test
    public void getResource() {

        ResourceHelper resourceHelper = new ResourceHelper();
        assertNotNull(resourceHelper.getResource("src/main/resources/DCCM-create.mp4"));
    }

    @Test
    public void resourceNotFound() {

        ResourceHelper resourceHelper = new ResourceHelper();
        assertNull(resourceHelper.getResource("src/main/resources/dummy.mp4"));
    }

    @Test
    public void getResources() {

        ResourceHelper resourceHelper = new ResourceHelper();
        List<String> resources = resourceHelper.getResources("src/main/resources");

        assertFalse("Empty folder", resources.isEmpty());
        assertTrue("File DCCM-create.mp4 not found", resources.contains("DCCM-create.mp4"));
    }

    @Test
    public void getResourceUrls() {

        ResourceHelper resourceHelper = new ResourceHelper();
        Map<String, String> resources = resourceHelper.getResourceUrls("src/main/resources", "http://localhost", "t1");

        assertFalse("Empty folder", resources.isEmpty());
        assertNotNull("Key 'DCCM-create.mp4' not found", resources.get("DCCM-create.mp4"));
        assertNotNull("Key 'play_icon_light_blue.png' not found", resources.get("play_icon_light_blue.png"));
    }


}