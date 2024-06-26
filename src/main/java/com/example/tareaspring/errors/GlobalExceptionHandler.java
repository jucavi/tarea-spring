package com.example.tareaspring.errors;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.DataFormatException;


@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
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


    @ExceptionHandler({
            CreateEntityException.class,
            MissingServletRequestParameterException.class,
            DataIntegrityViolationException.class,
            DatabaseSaveException.class,
            DateFormatException.class
    })
    public ResponseEntity<ApiError> handleDateFormatException(Exception ex) {

        String message = ex.getMessage();
        log.error(message);

        Map<String, String> errors = new HashMap<>();
        errors.put("system", message);

        return ResponseEntity.unprocessableEntity().body(new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now(),
                errors
        ));
    }

    @ExceptionHandler({
            DataFormatException.class,
            InvalidFormatException.class,
            HttpMessageNotReadableException.class,
            ValidSigningException.class
    })
    public ResponseEntity<ApiError> handleCreateEntityException(Exception ex) {

        String message = ex.getMessage();
        log.error(message);

        Map<String, String> errors = new HashMap<>();
        errors.put("date", message);

        return ResponseEntity.unprocessableEntity().body(new ApiError(
                HttpStatus.EXPECTATION_FAILED,
                LocalDateTime.now(),
                errors
        ));
    }
}
