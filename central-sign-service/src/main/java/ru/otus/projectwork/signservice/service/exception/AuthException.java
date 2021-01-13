package ru.otus.projectwork.signservice.service.exception;


public class AuthException extends RuntimeException {
    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
