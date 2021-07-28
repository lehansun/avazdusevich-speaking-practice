package com.lehansun.pet.project.controller.config;

import com.lehansun.pet.project.security.exception.JwtAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SpeakingPracticeAppExceptionHandler {

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<String> authenticationExceptionHandler(JwtAuthenticationException e) {
        System.err.println(e.getLocalizedMessage());
        return new ResponseEntity<>("Authentication denied: " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> unexpectedExceptionHandler(Exception e) {
        System.err.println(e.getLocalizedMessage());
        return new ResponseEntity<>("Unexpected: " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
