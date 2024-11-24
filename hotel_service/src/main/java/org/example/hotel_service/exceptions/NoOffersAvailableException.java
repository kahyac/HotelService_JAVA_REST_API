package org.example.hotel_service.exceptions;



public class NoOffersAvailableException extends RuntimeException {
    public NoOffersAvailableException(String message) {
        super(message);
    }
}
