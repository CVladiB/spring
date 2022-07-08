package ru.baranova.spring.services;

public interface LocaleService {

    String getMessage(String keyMessage);

    String getMessage(String keyMessage, Object... args);

}
