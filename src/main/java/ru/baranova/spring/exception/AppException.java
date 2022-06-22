package ru.baranova.spring.exception;

public class AppException extends RuntimeException {
    public AppException(Throwable e) {
        super(e);
    }
}
