package com.example.tareaspring.errors;

public class SquadNumberAlreadyTakenException extends RuntimeException {

    public SquadNumberAlreadyTakenException(String message) {
        super(message);
    }
}
