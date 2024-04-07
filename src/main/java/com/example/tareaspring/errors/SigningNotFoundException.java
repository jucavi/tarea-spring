package com.example.tareaspring.errors;

public class SigningNotFoundException extends RuntimeException {

    public SigningNotFoundException(Long id) {
        super("Unable to find signing with ID: " + id);
    }
}
