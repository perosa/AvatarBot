package com.perosa.avatarbot.controller;

import com.perosa.avatarbot.model.Session;
import com.perosa.avatarbot.model.SessionStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AvatarBotWebhookTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SessionStore sessionStore;

    @Test
    public void hi() throws Exception {

        given(sessionStore.getFrom(isA(String.class))).willReturn(new Session("0001"));

        InputStream inputStream = getClass().getResourceAsStream("/hi.json");

        byte[] targetArray = new byte[inputStream.available()];
        inputStream.read(targetArray);

        this.mockMvc.perform(post("/avatarbot/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(targetArray))
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void funTag() throws Exception {

        given(sessionStore.getFrom(isA(String.class))).willReturn(new Session("0001"));

        InputStream inputStream = getClass().getResourceAsStream("/funTag.json");

        byte[] targetArray = new byte[inputStream.available()];
        inputStream.read(targetArray);

        this.mockMvc.perform(post("/avatarbot/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(targetArray))
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void getAvatar() throws Exception {

        given(sessionStore.getFrom(isA(String.class))).willReturn(new Session("0001"));

        InputStream inputStream = getClass().getResourceAsStream("/getAvatar.json");

        byte[] targetArray = new byte[inputStream.available()];
        inputStream.read(targetArray);

        this.mockMvc.perform(post("/avatarbot/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(targetArray))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.outputContexts", hasSize(1)))
                .andExpect(jsonPath("$.outputContexts[0].name", is("projects/avatarbot-proj/agent/sessions/31575255/contexts/avatar")))
                .andExpect(jsonPath("$.outputContexts[0].parameters.avatarUrl", endsWith(".png")))
        ;
    }

}

