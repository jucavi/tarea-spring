package com.example.tareaspring.errors;

public class PlayerAlreadySignedException extends RuntimeException {
    public PlayerAlreadySignedException(String message) {
        super(message);
    }
}
