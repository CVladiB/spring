package ru.baranova.spring.services.message;

public interface LocaleService {

    String getMessage(String keyMessage);

    String getMessage(String keyMessage, Object... args);

}
