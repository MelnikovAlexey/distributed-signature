package ru.otus.projectwork.localsignservice.service.exception;


public class AuthException extends RuntimeException {
    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
