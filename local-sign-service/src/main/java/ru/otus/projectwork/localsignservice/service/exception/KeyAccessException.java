package ru.otus.projectwork.localsignservice.service.exception;

public class KeyAccessException extends RuntimeException {
    public KeyAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
