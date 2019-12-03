package com.perosa.avatarbot.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void hi() throws Exception {

        InputStream inputStream = getClass().getResourceAsStream("/hi.json");

        byte[] targetArray = new byte[inputStream.available()];
        inputStream.read(targetArray);

        this.mockMvc.perform(post("/avatarbot/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(targetArray))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.outputContexts", hasSize(0)))
        ;
    }

    @Test
    public void funTag() throws Exception {

        InputStream inputStream = getClass().getResourceAsStream("/funTag.json");

        byte[] targetArray = new byte[inputStream.available()];
        inputStream.read(targetArray);

        this.mockMvc.perform(post("/avatarbot/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(targetArray))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.outputContexts", hasSize(1)))
                .andExpect(jsonPath("$.outputContexts[0].name", is("tags")))
        ;
    }

}

