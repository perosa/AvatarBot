package com.perosa;

import com.perosa.avatarbot.util.ApplicationProperty;
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


    @Autowired
    private ApplicationProperty applicationProperty;

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


    public ApplicationProperty getApplicationProperty() {
        return applicationProperty;
    }

    public void setApplicationProperty(ApplicationProperty applicationProperty) {
        this.applicationProperty = applicationProperty;
    }

}