package ru.practicum.shareit.expections;

public class ValidationException extends RuntimeException {

    public ValidationException(final String message) {
        super(message);
    }
}
