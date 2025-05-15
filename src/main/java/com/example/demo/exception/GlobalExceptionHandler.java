package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UrlNullException.class})
    public ResponseEntity<String> handlerUrlNullException(UrlNullException e) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
    }

    @ExceptionHandler({UrlInvalidException.class})
    public ResponseEntity<String> handlerUrlInvalidException(UrlInvalidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler({NoUrlViewException.class})
    public ResponseEntity<String> handlerNoUrlViewException(NoUrlViewException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler({HashException.class})
    public ResponseEntity<String> handlerHashException(HashException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    @ExceptionHandler({EncryptException.class})
    public ResponseEntity<String> handlerEncryptException(HashException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler({UrlNotFoundException.class})
    public ResponseEntity<String> handlerUrlNotFoundException(UrlNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}