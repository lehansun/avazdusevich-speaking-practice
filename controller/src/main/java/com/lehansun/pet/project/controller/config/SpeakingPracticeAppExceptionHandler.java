package com.lehansun.pet.project.controller.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SpeakingPracticeAppExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> unexpectedExceptionHandler(Exception e) {
        System.err.println(e.getLocalizedMessage());
        return new ResponseEntity<>("Unexpected: " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
