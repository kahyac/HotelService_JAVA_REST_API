package com.example.agency.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN) // Retourne un code HTTP 403
public class SecurityException extends RuntimeException {
    public SecurityException(String message) {
        super(message);
    }
}
