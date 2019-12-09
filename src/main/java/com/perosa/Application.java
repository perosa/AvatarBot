package com.perosa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;

import javax.annotation.PostConstruct;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class Application  extends SpringBootServletInitializer {

    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());
    //


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void init() {

    }

//    //@Override
//    public void configureContentNegotiation(
//            ContentNegotiationConfigurer configurer) {
//        configurer.favorPathExtension(false);  // disabling auto media type from extension
//    }


}