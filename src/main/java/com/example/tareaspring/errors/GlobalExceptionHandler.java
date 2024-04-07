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
    public ResponseEntity<ApiError> handleMethodArgumentException(MethodArgumentNotValidException ex) {

        log.error(ex.getMessage());

        Map<String, String> errors = new HashMap<>();

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

        return ResponseEntity.unprocessableEntity().body(new ApiError(
                HttpStatus.UNPROCESSABLE_ENTITY,
                LocalDateTime.now(),
                errors
        ));
    }

    @ExceptionHandler({
            PlayerNotFoundException.class,
            TeamNotFoundException.class,
            SigningNotFoundException.class
    })
    public ResponseEntity<ApiError> handleNotFoundException(RuntimeException ex) {

        String message = ex.getMessage();
        log.error(message);

        Map<String, String> errors = new HashMap<>();
        errors.put("entity", message);

        return ResponseEntity.unprocessableEntity().body(new ApiError(
                HttpStatus.NOT_FOUND,
                LocalDateTime.now(),
                errors
        ));
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
