package ru.practicum.shareit.expections;

public class ConflictException extends RuntimeException {

    public ConflictException(final String message) {
        super(message);
    }
}
