package com.perosa.avatarbot.core;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class MatcherTest {

    @Autowired
    Matcher matcher;

    @Test
    void getPath() {

        List<String> tags = Arrays.asList(new String[]{"professional", "male"});
        assertEquals("src/main/resources/avatars/professional/male", matcher.getPath(tags));
    }

    @Test
    void match() {

        List<String> tags = Arrays.asList(new String[]{"professional", "male"});
        assertNotNull(matcher.match(tags, "https://localhost/"));
    }

    @Test
    void matchNotFound() {

        List<String> tags = Arrays.asList(new String[]{"not", "found"});
        assertNull(matcher.match(tags, "https://localhost/"));
    }

}