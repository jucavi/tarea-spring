package com.example.tareaspring.errors;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.*;


@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentException(MethodArgumentNotValidException ex) {

        log.error(ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        Map<String, Object> body = new HashMap<>();

        body.put("timestamp", LocalDateTime.now());
        body.put("reason", HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase());
        body.put("code", HttpStatus.UNPROCESSABLE_ENTITY.value());

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String message = error.getDefaultMessage();

            try {
                String field = ((FieldError) error).getField();
                errors.put(field, message);
            } catch (Exception e) {
                String field = error.getObjectName();
                errors.put(field, message);
            }
        });

        body.put("errors", errors);

        return ResponseEntity.unprocessableEntity().body(body);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, String>> handleException(Exception ex, WebRequest request) {
//
//        log.error(ex.getMessage());
//
//        Map<String, String> errors = new HashMap<>();
//        errors.put("field ", ex.getMessage());
//
//        return ResponseEntity.unprocessableEntity().body(errors);
//    }

}
