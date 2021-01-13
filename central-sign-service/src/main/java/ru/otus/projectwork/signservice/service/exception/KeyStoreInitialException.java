package ru.otus.projectwork.signservice.service.exception;

public class KeyStoreInitialException extends RuntimeException {
    public KeyStoreInitialException(String message) {
        super(message);
    }

    public KeyStoreInitialException(String message, Throwable cause) {
        super(message, cause);
    }
}
