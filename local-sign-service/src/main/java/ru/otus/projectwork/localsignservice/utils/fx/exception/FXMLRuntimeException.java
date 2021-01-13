package ru.otus.projectwork.localsignservice.utils.fx.exception;

public class FXMLRuntimeException extends RuntimeException{

    public FXMLRuntimeException(String s) {
        super(s);
    }

    public FXMLRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
