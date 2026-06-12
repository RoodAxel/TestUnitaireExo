package com.exemple.exception;

public class TicketInvalideException extends RuntimeException {

    public TicketInvalideException(String message) {
        super(message);
    }
}
