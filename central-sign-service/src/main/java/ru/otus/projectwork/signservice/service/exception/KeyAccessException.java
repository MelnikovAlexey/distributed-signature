package ru.otus.projectwork.signservice.service.exception;

public class KeyAccessException extends RuntimeException {
    public KeyAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
