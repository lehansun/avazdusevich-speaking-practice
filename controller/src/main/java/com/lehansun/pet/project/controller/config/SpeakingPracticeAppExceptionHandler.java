package com.lehansun.pet.project.controller.config;

import com.lehansun.pet.project.security.exception.JwtAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SpeakingPracticeAppExceptionHandler {

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<String> authenticationExceptionHandler(JwtAuthenticationException e) {
        System.err.println(e.getLocalizedMessage());
        return new ResponseEntity<>("Authentication denied: " + e.getLocalizedMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> unexpectedExceptionHandler(Exception e) {
        System.err.println(e.getLocalizedMessage());
        return new ResponseEntity<>("Unexpected: " + e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentExceptionHandler(Exception e) {
        System.err.println(e.getLocalizedMessage());
        return new ResponseEntity<>("Illegal argument: " + e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> usernameNotFoundExceptionHandler(UsernameNotFoundException e) {
        System.err.println(e.getLocalizedMessage());
        return new ResponseEntity<>("User not found: " + e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }
}
