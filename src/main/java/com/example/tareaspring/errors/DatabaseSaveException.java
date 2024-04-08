package com.example.tareaspring.errors;

public class DatabaseSaveException extends RuntimeException {

    public DatabaseSaveException(String message) {
        super(message);
    }
}
