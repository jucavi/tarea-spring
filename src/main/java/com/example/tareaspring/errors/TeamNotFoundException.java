package com.example.tareaspring.errors;

public class TeamNotFoundException extends RuntimeException {

    public TeamNotFoundException(Long id) {

        super("Unable to find a team with ID: " + id);
    }
}
