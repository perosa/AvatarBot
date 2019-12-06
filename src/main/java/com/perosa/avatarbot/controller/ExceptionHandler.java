package com.perosa.avatarbot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class ExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(ExceptionHandler.class.getName());

    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> genericHandler(HttpServletRequest request, RuntimeException ex) {

        LOGGER.log(Level.SEVERE, ex.getMessage(), ex);

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
    }

}


