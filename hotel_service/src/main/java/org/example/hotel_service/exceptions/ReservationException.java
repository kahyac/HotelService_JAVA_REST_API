package org.example.hotel_service.exceptions;

public class ReservationException extends RuntimeException {
    public ReservationException(String message) {
        super(message);
    }
}
