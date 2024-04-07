package com.example.tareaspring.errors;

public class CreateEntityException extends IllegalArgumentException {

    private String message;

    public CreateEntityException(String message) {
        super(message);
    }
}
