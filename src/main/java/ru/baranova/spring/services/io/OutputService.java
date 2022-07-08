package ru.baranova.spring.services.io;

public interface OutputService {
    void getMessage(String keyMessage);

    void getMessage(String keyMessage, Object... args);
}
