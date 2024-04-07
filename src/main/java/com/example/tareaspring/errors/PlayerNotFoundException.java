package com.example.tareaspring.errors;

public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(Long id) {
        super("Unable to find a player with ID: " + id);
    }
}
