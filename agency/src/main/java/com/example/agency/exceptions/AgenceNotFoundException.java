package com.example.agency.exceptions;

public class AgenceNotFoundException extends RuntimeException {
    public AgenceNotFoundException(String message) {
        super(message);
    }
}
